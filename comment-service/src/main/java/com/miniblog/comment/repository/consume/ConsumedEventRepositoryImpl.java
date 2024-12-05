package com.miniblog.comment.repository.consume;

import com.miniblog.comment.helper.SagaStatusUpdater;
import com.miniblog.comment.util.SagaStatus;
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