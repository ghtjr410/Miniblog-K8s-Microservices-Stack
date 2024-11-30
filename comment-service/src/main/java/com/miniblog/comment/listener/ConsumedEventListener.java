package com.miniblog.comment.listener;

import com.miniblog.comment.model.ConsumedEvent;
import com.miniblog.comment.util.SagaStatus;
import jakarta.persistence.PrePersist;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsumedEventListener {

    @PrePersist
    public void prePersist(ConsumedEvent consumedEvent) {
        if (consumedEvent.getSagaStatus() == null) {
            consumedEvent.setSagaStatus(SagaStatus.CREATED);
        }
        if (consumedEvent.getProcessed() == null) {
            consumedEvent.setProcessed(false);
        }
        log.info("ConsumedEvent is about to be created = {}", consumedEvent);
    }
}
