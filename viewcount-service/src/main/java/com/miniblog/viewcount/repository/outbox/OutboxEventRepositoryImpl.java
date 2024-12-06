package com.miniblog.viewcount.repository.outbox;

import com.miniblog.viewcount.helper.SagaStatusUpdater;
import com.miniblog.viewcount.util.SagaStatus;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OutboxEventRepositoryImpl implements OutboxEventRepositoryCustom {
    private final SagaStatusUpdater sagaStatusUpdater;

    public boolean updateSagaStatus(UUID eventUuid, SagaStatus[] expectedCurrentStatus, SagaStatus newStatus, Boolean processed) {
        return sagaStatusUpdater.updateSagaStatus("OutboxEvent", eventUuid, expectedCurrentStatus, newStatus, processed);
    }
}
