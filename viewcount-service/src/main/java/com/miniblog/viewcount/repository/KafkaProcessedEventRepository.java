package com.miniblog.viewcount.repository;

import com.miniblog.viewcount.model.KafkaProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface KafkaProcessedEventRepository extends BaseRepository<KafkaProcessedEvent, UUID>{
    boolean existsByEventUuid(UUID eventUuid);
    Optional<KafkaProcessedEvent> findByEventUuid(UUID eventUuid);
    Optional<KafkaProcessedEvent> findByProcessedFalseAndEventUuid(UUID eventUuid);
}
