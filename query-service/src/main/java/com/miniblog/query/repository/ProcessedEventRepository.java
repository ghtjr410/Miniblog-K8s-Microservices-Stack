package com.miniblog.query.repository;

import com.miniblog.query.model.ProcessedEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProcessedEventRepository extends MongoRepository<ProcessedEvent, String>, ProcessedEventRepositoryCustom {
    boolean existsByEventUuid(String eventUuid);
    Optional<ProcessedEvent> findByEventUuid(String eventUuid);
}
