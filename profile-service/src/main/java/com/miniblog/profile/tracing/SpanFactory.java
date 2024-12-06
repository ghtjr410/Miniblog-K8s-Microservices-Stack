package com.miniblog.profile.tracing;

import brave.propagation.TraceContext;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.brave.bridge.BraveSpan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class SpanFactory {
    private final brave.Tracer braveTracer;

    public Span createSpanFromTraceId(String traceIdString) {
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

        // Brave Tracer를 사용하여 Span 생성
        brave.Span braveSpan = braveTracer.toSpan(traceContext).name("OutboxEventProcessing").start();

        // Brave Span을 Micrometer Span으로 래핑
        return new BraveSpan(braveSpan);
    }
}
