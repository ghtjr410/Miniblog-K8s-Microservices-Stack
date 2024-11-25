package com.miniblog.viewcount.repository;

import com.miniblog.viewcount.model.ReceivedEvent;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReceivedEventRepository extends BaseRepository<ReceivedEvent, UUID>{
    boolean existsByEventUuid(UUID eventUuid);
    Optional<ReceivedEvent> findByEventUuid(UUID eventUuid);
    Optional<ReceivedEvent> findByProcessedFalseAndEventUuid(UUID eventUuid);
}
