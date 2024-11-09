package com.miniblog.post.service;

import brave.propagation.TraceContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniblog.post.avro.PostCreatedEvent;
import com.miniblog.post.model.OutboxEvent;
import com.miniblog.post.producer.PostCreatedEventProducer;
import com.miniblog.post.repository.OutboxEventRepository;
import com.miniblog.post.util.SagaStatus;
import io.micrometer.tracing.Span;

import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.Tracer.SpanInScope;
import io.micrometer.tracing.brave.bridge.BraveSpan;
import io.micrometer.tracing.brave.bridge.BraveTracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxEventProcessor {
    private final OutboxEventRepository outboxEventRepository;
    private final PostCreatedEventProducer postCreatedEventProducer;
    private final ObjectMapper objectMapper;
    private final Tracer tracer; // Micrometer Tracer
    private final brave.Tracer braveTracer; // Brave Tracer


    public void processEvent(OutboxEvent outboxEvent) {
        try {
            // Saga 상태를 PROCESSING으로 업데이트
            outboxEvent.setSagaStatus(SagaStatus.PROCESSING);
            outboxEventRepository.save(outboxEvent);
            PostCreatedEvent postCreatedEvent = objectMapper.readValue(outboxEvent.getPayload(), PostCreatedEvent.class);

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

            // Brave Tracer를 사용하여 Span 생성
            brave.Span braveSpan = braveTracer.toSpan(traceContext).name("OutboxEventProcessing").start();

            // Brave Span을 Micrometer Span으로 래핑
            Span newSpan = new BraveSpan(braveSpan);

            try (SpanInScope ws = tracer.withSpan(newSpan)){
                postCreatedEventProducer.publishEvent(outboxEvent.getEventUuId(), postCreatedEvent, outboxEvent, newSpan);
            } finally {
                newSpan.end();
            }
        } catch (Exception e){
            log.error("Error processing OutboxEvent : {}", e.getMessage());
        }
    }
}
