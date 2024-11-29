package com.miniblog.post.service.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniblog.post.avro.PostCreatedEvent;
import com.miniblog.post.avro.PostDeletedEvent;
import com.miniblog.post.avro.PostUpdatedEvent;
import com.miniblog.post.handler.produce.EventProducerHandler;
import com.miniblog.post.handler.produce.EventProducerHandlerRegistry;
import com.miniblog.post.model.OutboxEvent;
import com.miniblog.post.producer.EventProducer;
import com.miniblog.post.tracing.SpanFactory;
import com.miniblog.post.util.ProducedEventType;
import com.miniblog.post.util.SagaStatus;
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
    private final EventProducerHandlerRegistry handlerRegistry;
    private final OutboxEventService outboxEventService;


    public void processEvent(OutboxEvent outboxEvent) {
        try {
            // 1. PROCESSING 상태로 업데이트
            boolean updated = outboxEventService.updateStatus(outboxEvent.getEventUuid().toString(), SagaStatus.CREATED, SagaStatus.PROCESSING, null);
            if (!updated) {
                log.info("Event with UUID {} is already being processed or has invalid status.", outboxEvent.getEventUuid());
                return; // 다른 스레드에서 처리 중이므로 메서드 종료
            }
            // 2. 이벤트 핸들러 가져오기
            EventProducerHandler handler = handlerRegistry.getHandler(outboxEvent.getEventType());
            if (handler == null) {
                throw new IllegalArgumentException("Unsupported event type: " + outboxEvent.getEventType());
            }
            // 3. 이벤트 처리
            handler.handleEvent(outboxEvent);
            // 4. 완료 상태 업데이트 및 processed 필드 저장
            outboxEventService.markEventAsCompleted(outboxEvent);
            log.info("Event publish Success");
        } catch (Exception ex) {
            outboxEventService.markEventAsFailed(outboxEvent);
            log.error("Error processing OutboxEvent: {}", ex.getMessage(), ex);
        }
    }
}