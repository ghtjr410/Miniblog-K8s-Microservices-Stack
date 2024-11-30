package com.miniblog.comment.listener;

import com.miniblog.comment.model.OutboxEvent;
import com.miniblog.comment.util.SagaStatus;
import jakarta.persistence.PrePersist;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OutboxEventListener {

    @PrePersist
    public void prePersist(OutboxEvent outboxEvent) {
        if (outboxEvent.getSagaStatus() == null) {
            outboxEvent.setSagaStatus(SagaStatus.CREATED);
        }
        if (outboxEvent.getProcessed() == null) {
            outboxEvent.setProcessed(false);
        }
        log.info("OutboxEvent is about to be created = {}", outboxEvent);
    }
}
