package com.miniblog.viewcount.listener;

import com.miniblog.viewcount.model.ReceivedEvent;
import com.miniblog.viewcount.util.SagaStatus;
import jakarta.persistence.PrePersist;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
public class ReceivedEventListener {

    @PrePersist
    public void prePersist(ReceivedEvent kafkaProcessedEvent) {
        if (kafkaProcessedEvent.getSagaStatus() == null) {
            kafkaProcessedEvent.setSagaStatus(SagaStatus.PROCESSING);
        }
        if (kafkaProcessedEvent.getCreatedDate() == null) {
            kafkaProcessedEvent.setCreatedDate(Instant.now());
        }
        if (kafkaProcessedEvent.getProcessed() == null) {
            kafkaProcessedEvent.setProcessed(false);
        }
        log.info("KafkaProcessed is about to be created = {}", kafkaProcessedEvent);
    }
}
