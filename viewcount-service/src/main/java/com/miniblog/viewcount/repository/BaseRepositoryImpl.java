package com.miniblog.viewcount.repository;

import com.miniblog.viewcount.util.SagaStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.util.Arrays;
import java.util.UUID;

public class BaseRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements BaseRepository<T,ID> {
    private final EntityManager entityManager;
    private final Class<T> domainClass;

    public BaseRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
        this.domainClass = entityInformation.getJavaType();
    }

    @Override
    public boolean updateSagaStatus(UUID eventUuid, SagaStatus[] expectedCurrentStatus, SagaStatus newStatus, Boolean processed) {
        String entityName = domainClass.getSimpleName();

        String jpql = "UPDATE " + entityName + " o SET o.sagaStatus = :newStatus";
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
