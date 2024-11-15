package com.miniblog.comment.service;

import brave.propagation.TraceContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniblog.comment.avro.CommentCreatedEvent;
import com.miniblog.comment.avro.CommentDeletedEvent;
import com.miniblog.comment.avro.CommentUpdatedEvent;
import com.miniblog.comment.model.OutboxEvent;
import com.miniblog.comment.producer.EventProducer;
import com.miniblog.comment.repository.OutboxEventRepository;
import com.miniblog.comment.util.SagaStatus;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.Tracer.SpanInScope;
import io.micrometer.tracing.brave.bridge.BraveSpan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxEventProcessor {
    private final OutboxEventRepository outboxEventRepository;
    private final EventProducer eventProducer;
    private final ObjectMapper objectMapper;
    private final Tracer tracer;
    private final brave.Tracer braveTracer;

    @Value("${comment.created.event.topic.name}")
    private String commentCreatedTopicName;
    @Value("${comment.updated.event.topic.name}")
    private String commentUpdatedTopicName;
    @Value("${comment.deleted.event.topic.name}")
    private String commentDeletedTopicName;

    public void processCommentCreatedEvent(OutboxEvent outboxEvent) {
        try {
            outboxEvent.setSagaStatus(SagaStatus.PROCESSING);
            outboxEventRepository.save(outboxEvent);
            CommentCreatedEvent commentCreatedEvent = objectMapper.readValue(outboxEvent.getPayload(), CommentCreatedEvent.class);

            String traceIdString = outboxEvent.getTraceId();

            // 128비트 traceId 문자열을 두 개의 long 값으로 분리
            BigInteger traceIdBigInt = new BigInteger(traceIdString, 16);
            long traceIdHigh = traceIdBigInt.shiftRight(64).longValue();
            long traceIdLow = traceIdBigInt.longValue();

            long spanId;
            do {
                spanId = ThreadLocalRandom.current().nextLong();
            } while (spanId == 0);

            // TraceContext 생성
            TraceContext traceContext = TraceContext.newBuilder()
                    .traceIdHigh(traceIdHigh)
                    .traceId(traceIdLow)
                    .spanId(spanId)
                    .sampled(true)
                    .build();
            
            // Braver Tracer를 사용하여 Span 생성
            brave.Span braveSpan = braveTracer.toSpan(traceContext).name("OutboxEventProcessing").start();

            // Brave Span을 Micrometer Span으로 래핑
            Span newSpan = new BraveSpan(braveSpan);

            try (SpanInScope ws = tracer.withSpan(newSpan)) {
                eventProducer.publishEvent(commentCreatedTopicName, outboxEvent.getEventUuid(), commentCreatedEvent, outboxEvent, newSpan);
            } finally {
                newSpan.end();
            }
        } catch (Exception ex) {
            log.error("Error processing OutboxEvent : {}", ex.getMessage());
        }
    }

    public void processCommentUpdatedEvent(OutboxEvent outboxEvent) {
        try {
            outboxEvent.setSagaStatus(SagaStatus.PROCESSING);
            outboxEventRepository.save(outboxEvent);
            CommentUpdatedEvent commentUpdatedEvent = objectMapper.readValue(outboxEvent.getPayload(), CommentUpdatedEvent.class);

            String traceIdString = outboxEvent.getTraceId();

            // 128비트 traceId 문자열을 두 개의 long 값으로 분리
            BigInteger traceIdBigInt = new BigInteger(traceIdString, 16);
            long traceIdHigh = traceIdBigInt.shiftRight(64).longValue();
            long traceIdLow = traceIdBigInt.longValue();

            long spanId;
            do {
                spanId = ThreadLocalRandom.current().nextLong();
            } while (spanId == 0);

            // TraceContext 생성
            TraceContext traceContext = TraceContext.newBuilder()
                    .traceIdHigh(traceIdHigh)
                    .traceId(traceIdLow)
                    .spanId(spanId)
                    .sampled(true)
                    .build();

            // Braver Tracer를 사용하여 Span 생성
            brave.Span braveSpan = braveTracer.toSpan(traceContext).name("OutboxEventProcessing").start();

            // Brave Span을 Micrometer Span으로 래핑
            Span newSpan = new BraveSpan(braveSpan);

            try (SpanInScope ws = tracer.withSpan(newSpan)) {
                eventProducer.publishEvent(commentUpdatedTopicName, outboxEvent.getEventUuid(), commentUpdatedEvent, outboxEvent, newSpan);
            } finally {
                newSpan.end();
            }
        } catch (Exception ex) {
            log.error("Error processing OutboxEvent : {}", ex.getMessage());
        }
    }

    public void processCommentDeletedEvent(OutboxEvent outboxEvent) {
        try {
            outboxEvent.setSagaStatus(SagaStatus.PROCESSING);
            outboxEventRepository.save(outboxEvent);
            CommentDeletedEvent commentDeletedEvent = objectMapper.readValue(outboxEvent.getPayload(), CommentDeletedEvent.class);

            String traceIdString = outboxEvent.getTraceId();

            //128비트 traceId 문자열을 두 개의 long 값으로 분리
            BigInteger traceIdBigInt = new BigInteger(traceIdString, 16);
            long traceIdHigh = traceIdBigInt.shiftRight(64).longValue();
            long traceIdLow = traceIdBigInt.longValue();

            long spanId;
            do {
                spanId = ThreadLocalRandom.current().nextLong();
            } while (spanId == 0);

            // TraceContext 생성
            TraceContext traceContext = TraceContext.newBuilder()
                    .traceIdHigh(traceIdHigh)
                    .traceId(traceIdLow)
                    .spanId(spanId)
                    .sampled(true)
                    .build();

            // Braver Tracer를 사용하여 Span 생성
            brave.Span braveSpan = braveTracer.toSpan(traceContext).name("OutboxEventProcessing").start();

            // Brave Span을 Micrometer Span으로 래핑
            Span newSpan = new BraveSpan(braveSpan);

            try (SpanInScope ws = tracer.withSpan(newSpan)) {
                eventProducer.publishEvent(commentDeletedTopicName, outboxEvent.getEventUuid(), commentDeletedEvent, outboxEvent, newSpan);
            } finally {
                newSpan.end();
            }
        } catch (Exception ex) {
            log.error("Error processing OutboxEvent : {}", ex.getMessage());
        }
    }
}
