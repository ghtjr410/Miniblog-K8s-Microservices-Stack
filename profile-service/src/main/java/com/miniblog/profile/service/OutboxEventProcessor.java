package com.miniblog.profile.service;

import brave.propagation.TraceContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniblog.profile.avro.ProfileCreatedEvent;
import com.miniblog.profile.avro.ProfileUpdatedEvent;
import com.miniblog.profile.model.OutboxEvent;
import com.miniblog.profile.producer.EventProducer;
import com.miniblog.profile.repository.OutboxEventRepository;
import com.miniblog.profile.util.SagaStatus;
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
    private final brave.Tracer braverTracer;

    @Value("${profile.created.event.topic.name}")
    private String profileCreatedTopicName;

    @Value("${profile.updated.event.topic.name}")
    private String profileUpdatedTopicName;

    public void processProfileCreatedEvent(OutboxEvent outboxEvent) {
        try {
            outboxEvent.setSagaStatus(SagaStatus.PROCESSING);
            outboxEventRepository.save(outboxEvent);
            ProfileCreatedEvent profileCreatedEvent = objectMapper.readValue(outboxEvent.getPayload(), ProfileCreatedEvent.class);

            String traceIdString = outboxEvent.getTraceId();

            // 128비트 traceId 문자열을 두 개의 long 값으로 분리
            BigInteger traceIdBigInt = new BigInteger(traceIdString, 16);
            long traceIdHigh = traceIdBigInt.shiftRight(64).longValue();
            long traceIdLow = traceIdBigInt.longValue();

            // 랜덤한 spanId 생성
            long spanId;
            do{
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
            brave.Span braveSpan = braverTracer.toSpan(traceContext).name("OutboxEventProcessing").start();

            // Brave Span을 Micrometer Span으로 래핑
            Span newSpan = new BraveSpan(braveSpan);

            try (SpanInScope ws = tracer.withSpan(newSpan)) {
                eventProducer.publishEvent(profileCreatedTopicName, outboxEvent.getEventUuid(), profileCreatedEvent, outboxEvent, newSpan);
            } finally {
                newSpan.end();
            }
        } catch (Exception ex) {
            log.error("Error processing OutboxEvent : {}", ex.getMessage());
        }
    }

    public void processProfileUpdatedEvent(OutboxEvent outboxEvent) {
        try {
            outboxEvent.setSagaStatus(SagaStatus.PROCESSING);
            outboxEventRepository.save(outboxEvent);
            ProfileUpdatedEvent profileUpdatedEvent = objectMapper.readValue(outboxEvent.getPayload(), ProfileUpdatedEvent.class);
            String traceIdString = outboxEvent.getTraceId();

            // 128비트 traceId 문자열을 두 개의 long 값으로 분리
            BigInteger traceIdBigInt = new BigInteger(traceIdString, 16);
            long traceIdHigh = traceIdBigInt.shiftRight(64).longValue();
            long traceIdLow = traceIdBigInt.longValue();

            // 랜덤한 spanId 생성
            long spanId;
            do{
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
            brave.Span braveSpan = braverTracer.toSpan(traceContext).name("OutboxEventProcessing").start();

            // Brave Span을 Micrometer Span으로 래핑
            Span newSpan = new BraveSpan(braveSpan);

            try (SpanInScope ws = tracer.withSpan(newSpan)) {
                eventProducer.publishEvent(profileUpdatedTopicName, outboxEvent.getEventUuid(), profileUpdatedEvent, outboxEvent, newSpan);
            } finally {
                newSpan.end();
            }
        } catch (Exception ex) {
            log.error("Error processing OutboxEvent : {}", ex.getMessage());
        }
    }
}
