package com.miniblog.like.repository.consume;

import com.miniblog.like.model.ConsumedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConsumedEventRepository extends JpaRepository<ConsumedEvent, UUID>, ConsumedEventRepositoryCustom {
    boolean existsByEventUuid(UUID eventUuid);
    Optional<ConsumedEvent> findByEventUuid(UUID eventUuid);
    Optional<ConsumedEvent> findByProcessedFalseAndEventUuid(UUID eventUuid);
}
