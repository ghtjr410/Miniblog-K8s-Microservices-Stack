package com.miniblog.profile.repository.outbox;

import com.miniblog.profile.helper.SagaStatusUpdater;
import com.miniblog.profile.util.SagaStatus;
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
