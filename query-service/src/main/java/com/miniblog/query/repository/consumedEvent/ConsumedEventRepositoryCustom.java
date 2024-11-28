package com.miniblog.query.repository.consumedEvent;

import com.miniblog.query.model.ConsumedEvent;
import com.miniblog.query.util.ConsumedEventType;
import com.miniblog.query.util.SagaStatus;

public interface ConsumedEventRepositoryCustom {
    boolean updateSagaStatus(String eventUuid, SagaStatus[] expectedCurrentStatus, SagaStatus newStatus, Boolean processed);
    ConsumedEvent upsertEventToProcessing(String eventUuid, ConsumedEventType eventType, SagaStatus newStatus);
}
