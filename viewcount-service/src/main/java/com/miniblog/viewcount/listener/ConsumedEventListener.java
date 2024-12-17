package com.miniblog.viewcount.listener;

import com.miniblog.viewcount.model.ConsumedEvent;
import com.miniblog.viewcount.util.SagaStatus;
import jakarta.persistence.PrePersist;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
public class ConsumedEventListener {

    @PrePersist
    public void prePersist(ConsumedEvent consumedEvent) {
        if (consumedEvent.getSagaStatus() == null) {
            consumedEvent.setSagaStatus(SagaStatus.PROCESSING);
        }
        if (consumedEvent.getCreatedDate() == null) {
            consumedEvent.setCreatedDate(Instant.now());
        }
        if (consumedEvent.getProcessed() == null) {
            consumedEvent.setProcessed(false);
        }
        log.info("ConsumedEvent is about to be created = {}", consumedEvent);
    }
}
