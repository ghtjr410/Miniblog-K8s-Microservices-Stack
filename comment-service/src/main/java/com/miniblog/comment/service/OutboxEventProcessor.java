package com.miniblog.comment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniblog.comment.avro.CommentCreatedEvent;
import com.miniblog.comment.avro.CommentDeletedEvent;
import com.miniblog.comment.avro.CommentUpdatedEvent;
import com.miniblog.comment.model.OutboxEvent;
import com.miniblog.comment.producer.EventProducer;
import com.miniblog.comment.repository.OutboxEventRepository;
import com.miniblog.comment.tracing.SpanFactory;
import com.miniblog.comment.util.EventType;
import com.miniblog.comment.util.SagaStatus;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.Tracer.SpanInScope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxEventProcessor {
    private final OutboxEventRepository outboxEventRepository;
    private final EventProducer eventProducer;
    private final ObjectMapper objectMapper;
    private final Tracer tracer;
    private final SpanFactory spanFactory;
    private final OutboxEventService outboxEventService;

    @Value("${comment.created.event.topic.name}")
    private String commentCreatedTopicName;
    @Value("${comment.updated.event.topic.name}")
    private String commentUpdatedTopicName;
    @Value("${comment.deleted.event.topic.name}")
    private String commentDeletedTopicName;

    public void processEvent(OutboxEvent outboxEvent) {
        Span newSpan = null;
        try {
            // 1. PROCESSING 상태로 업데이트
            log.info("Created일때 상태변경");
            boolean updated = outboxEventService.updateStatus(outboxEvent.getEventUuid(), SagaStatus.CREATED, SagaStatus.PROCESSING, null);
            if (!updated) {
                log.info("Event with UUID {} is already being processed or has invalid status.", outboxEvent.getEventUuid());
                return; // 다른 스레드에서 처리 중이므로 메서드 종료
            }

            // 2. 이벤트 타입 확인
            String topicName = getTopicName(outboxEvent.getEventType());
            Class<? extends SpecificRecordBase> eventClass = getEventClass(outboxEvent.getEventType());

            // 3. 페이로드 역직렬화
            SpecificRecordBase eventObject = objectMapper.readValue(outboxEvent.getPayload(), eventClass);

            // 4. Span 생성
            newSpan = spanFactory.createSpanFromTraceId(outboxEvent.getTraceId());

            // 5. 이벤트 발행
            try (SpanInScope ws = tracer.withSpan(newSpan)) {
                eventProducer.publishEvent(topicName, outboxEvent.getEventUuid(), eventObject, newSpan);
            }
            // 6. 완료 상태 업데이트 및 processed 필드 저장 (별도의 트랜잭션)
            outboxEventService.markEventAsCompleted(outboxEvent);
        } catch (Exception ex) {
            // 7. 실패 상태 업데이트 (별도의 트랜잭션)
            outboxEventService.markEventAsFailed(outboxEvent);
            log.error("Error processing OutboxEvent: {}", ex.getMessage(), ex);
        } finally {
            if (newSpan != null) {
                newSpan.end();
            }
        }
    }



    private String getTopicName(EventType eventType) {
        switch (eventType) {
            case COMMENT_CREATED:
                return commentCreatedTopicName;
            case COMMENT_UPDATED:
                return commentUpdatedTopicName;
            case COMMENT_DELETED:
                return commentDeletedTopicName;
            default:
                throw new IllegalArgumentException("Unsupported event type: " + eventType);
        }
    }

    private Class<? extends SpecificRecordBase> getEventClass(EventType eventType) {
        switch (eventType) {
            case COMMENT_CREATED:
                return CommentCreatedEvent.class;
            case COMMENT_UPDATED:
                return CommentUpdatedEvent.class;
            case COMMENT_DELETED:
                return CommentDeletedEvent.class;
            default:
                throw new IllegalArgumentException("Unsupported event type: " + eventType);
        }
    }
}