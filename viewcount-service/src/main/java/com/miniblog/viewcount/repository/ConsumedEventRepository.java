package com.miniblog.viewcount.repository;

import com.miniblog.viewcount.model.ConsumedEvent;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConsumedEventRepository extends BaseRepository<ConsumedEvent, UUID>{
    boolean existsByEventUuid(UUID eventUuid);
    Optional<ConsumedEvent> findByEventUuid(UUID eventUuid);
    Optional<ConsumedEvent> findByProcessedFalseAndEventUuid(UUID eventUuid);
}
