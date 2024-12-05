package com.miniblog.like.repository.outbox;

import com.miniblog.like.helper.SagaStatusUpdater;
import com.miniblog.like.util.SagaStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OutboxEventRepositoryImpl implements OutboxEventRepositoryCustom {
    private final SagaStatusUpdater sagaStatusUpdater;

    public boolean updateSagaStatus(UUID eventUuid, SagaStatus[] expectedCurrentStatus, SagaStatus newStatus, Boolean processed) {
        return sagaStatusUpdater.updateSagaStatus("OutboxEvent", eventUuid, expectedCurrentStatus, newStatus, processed);
    }
}
