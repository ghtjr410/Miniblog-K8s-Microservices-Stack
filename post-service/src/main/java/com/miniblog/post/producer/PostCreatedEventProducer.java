package com.miniblog.post.producer;

import com.miniblog.post.avro.PostCreatedEvent;
import com.miniblog.post.model.OutboxEvent;
import com.miniblog.post.repository.OutboxEventRepository;
import com.miniblog.post.util.SagaStatus;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
public class PostCreatedEventProducer {
    private final KafkaTemplate<String, PostCreatedEvent> kafkaTemplate;
    private final OutboxEventRepository outboxEventRepository;
    private final Tracer tracer;

    @Value("${post.created.event.topic.name}")
    private String topicName;

    @Retryable(
            value = { Exception.class },
            maxAttempts = 5,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void publishEvent(String key, PostCreatedEvent postCreatedEvent, OutboxEvent outboxEvent, Span span) {
        RetryContext context = RetrySynchronizationManager.getContext();
        int retryCount = context != null ? context.getRetryCount() : 0;
        log.info("이벤트 발행 시도 중, key: {}, attempt number: {}", key, retryCount + 1);

        try (Tracer.SpanInScope ws = tracer.withSpan(span)){
            try {
                kafkaTemplate.send(topicName, key, postCreatedEvent).get(); // 동기화하여 예외 처리
                handleSuccess(outboxEvent);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @Recover
    public void recover(Exception ex, String key, PostCreatedEvent postCreatedEvent, OutboxEvent outboxEvent, Span span) {
        // 실패 시에도 Span을 현재 컨텍스트에 설정
        try (Tracer.SpanInScope ws = tracer.withSpan(span)) {
            handleFailure(outboxEvent, ex);
        }
    }

    private void handleSuccess(OutboxEvent outboxEvent) {
        outboxEvent.setProcessed(true);
        outboxEvent.setSagaStatus(SagaStatus.COMPLETED);
        outboxEventRepository.save(outboxEvent);
        log.info("Event Successfully processed and marked as COMPLETED.");
    }
    private void handleFailure(OutboxEvent outboxEvent, Exception ex) {
        log.error("Failed to send message after retries: {}", ex.getMessage());
        // 실패에 대한 처리 로직 추가 (예: 상태 업데이트)
        outboxEvent.setSagaStatus(SagaStatus.FAILED);
        outboxEventRepository.save(outboxEvent);
    }
}
