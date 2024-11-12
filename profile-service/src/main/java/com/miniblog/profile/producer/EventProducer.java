package com.miniblog.profile.producer;

import com.miniblog.profile.avro.ProfileCreatedEvent;
import com.miniblog.profile.model.OutboxEvent;
import com.miniblog.profile.repository.OutboxEventRepository;
import com.miniblog.profile.util.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
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
public class EventProducer {
    private final KafkaTemplate<String, SpecificRecordBase> kafkaTemplate;
    private final OutboxEventRepository outboxEventRepository;

    @Retryable(
            value = {Exception.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public <T extends SpecificRecordBase> void publishEvent(String topicName, String key, T event, OutboxEvent outboxEvent) {
        RetryContext context = RetrySynchronizationManager.getContext();
        int retryCount = context != null ? context.getRetryCount() : 0;
        log.info("이벤트 발행 시도 중, key: {}, attempt number: {}", key, retryCount + 1);

        try {
            log.info("topicName = {}, key = {}, event = {}", topicName, key, event);
            kafkaTemplate.send(topicName, key, event).get(); // 동기화하여 예외처리
            handleSuccess(outboxEvent);
        } catch (InterruptedException | ExecutionException e) {
            Throwable realException = e.getCause();
            log.error("직렬화 오류 발생: {}", realException.getMessage(), realException);
            throw new RuntimeException(e);
        }
    }

    @Recover
    public void recover(Exception ex, String topicName, String key, SpecificRecordBase event, OutboxEvent outboxEvent) {
        log.error("이벤트 발행 실패: {}", ex.getMessage(), ex);
        handleFailure(outboxEvent, ex);
    }

    private void handleSuccess(OutboxEvent outboxEvent) {
        outboxEvent.setProcessed(true);
        outboxEvent.setSagaStatus(SagaStatus.COMPLETED);
        outboxEventRepository.save(outboxEvent);
        log.info("Event Successfully processed and marked as COMPLETED.");
    }

    private void handleFailure(OutboxEvent outboxEvent, Exception ex) {
        outboxEvent.setSagaStatus(SagaStatus.FAILED);
        outboxEventRepository.save(outboxEvent);
        log.error("Failed to send message after retries: {}", ex.getMessage());
    }
}
