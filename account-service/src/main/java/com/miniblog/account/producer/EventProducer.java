package com.miniblog.account.producer;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.Tracer.SpanInScope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.RetryContext;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetrySynchronizationManager;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventProducer {
    private final KafkaTemplate<String, SpecificRecordBase> kafkaTemplate;
    private final Tracer tracer;

    @Retryable(
            value = {Exception.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public <T extends SpecificRecordBase> void publishEvent(String topicName, String key, T event, Span span) {
        RetryContext context = RetrySynchronizationManager.getContext();
        int retryCount = context != null ? context.getRetryCount() : 0;
        log.info("이벤트 발행 시도 중, key: {}, attempt number: {}", key, retryCount + 1);

        try (SpanInScope ws = tracer.withSpan(span)) {
            try {
                log.info("topicName = {}, key = {}, event = {}", topicName, key, event);
                kafkaTemplate.send(topicName, key, event).get(); // 동기화하여 예외처리
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Recover
    public void recover(Exception ex, String topicName, String key, SpecificRecordBase event, Span span) {
        throw new RuntimeException("재시도 전부 실패");
    }
}

