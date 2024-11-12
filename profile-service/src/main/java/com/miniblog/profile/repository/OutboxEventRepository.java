package com.miniblog.profile.repository;

import com.miniblog.profile.model.OutboxEvent;
import com.miniblog.profile.util.SagaStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
    List<OutboxEvent> findByProcessedFalseAndSagaStatus(SagaStatus sagaStatus);
}
