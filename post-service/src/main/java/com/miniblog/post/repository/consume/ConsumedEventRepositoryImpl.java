package com.miniblog.post.repository.consume;

import com.miniblog.post.helper.SagaStatusUpdater;
import com.miniblog.post.util.SagaStatus;
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
