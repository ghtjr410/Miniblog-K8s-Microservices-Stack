package com.miniblog.like.helper;

import com.miniblog.like.util.SagaStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.UUID;

@RequiredArgsConstructor
public class SagaStatusUpdater {
    private final EntityManager entityManager;

    public boolean updateSagaStatus(String domainClass, UUID eventUuid, SagaStatus[] expectedCurrentStatus, SagaStatus newStatus, Boolean processed) {
        String jpql = "UPDATE " + domainClass + " o SET o.sagaStatus = :newStatus";
        if (processed != null) {
            jpql += ", o.processed = :processed";
        }
        jpql += " WHERE o.eventUuid = :eventUuid AND o.sagaStatus IN :expectedCurrentStatus";

        Query query = entityManager.createQuery(jpql);
        query.setParameter("newStatus", newStatus);
        if (processed != null) {
            query.setParameter("processed", processed);
        }
        query.setParameter("eventUuid", eventUuid);
        query.setParameter("expectedCurrentStatus", Arrays.asList(expectedCurrentStatus));

        int rowsUpdated = query.executeUpdate();
        return rowsUpdated > 0;
    }
}
