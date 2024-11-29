package com.miniblog.post.repository.outbox;

import com.miniblog.post.util.SagaStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OutboxEventRepositoryImpl implements OutboxEventRepositoryCustom{
    private final EntityManager entityManager;

    public boolean updateSagaStatus(UUID eventUuid, SagaStatus[] expectedCurrentStatus, SagaStatus newStatus, Boolean processed) {
        String jpql = "UPDATE OutboxEvent o SET o.sagaStatus = :newStatus";
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
