package com.miniblog.viewcount.repository;

import com.miniblog.viewcount.model.OutboxEvent;
import com.miniblog.viewcount.util.SagaStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID>, OutboxEventRepositoryCustom{
    List<OutboxEvent> findByProcessedFalseAndSagaStatus(SagaStatus sagaStatus);
}
