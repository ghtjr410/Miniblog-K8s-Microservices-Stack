package com.miniblog.comment.repository.outbox;

import com.miniblog.comment.model.OutboxEvent;
import com.miniblog.comment.util.SagaStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long>, OutboxEventRepositoryCustom {
    List<OutboxEvent> findByProcessedFalseAndSagaStatus(SagaStatus sagaStatus);
}