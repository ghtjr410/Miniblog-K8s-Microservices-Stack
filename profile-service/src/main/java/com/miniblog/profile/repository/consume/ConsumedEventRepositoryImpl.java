package com.miniblog.profile.repository.consume;

import com.miniblog.profile.helper.SagaStatusUpdater;
import com.miniblog.profile.util.SagaStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ConsumedEventRepositoryImpl implements ConsumedEventRepositoryCustom {
    private final SagaStatusUpdater sagaStatusUpdater;

    public boolean updateSagaStatus(UUID eventUuid, SagaStatus[] expectedCurrentStatus, SagaStatus newStatus, Boolean processed) {
        return sagaStatusUpdater.updateSagaStatus("ConsumedEvent", eventUuid, expectedCurrentStatus, newStatus, processed);
    }
}
