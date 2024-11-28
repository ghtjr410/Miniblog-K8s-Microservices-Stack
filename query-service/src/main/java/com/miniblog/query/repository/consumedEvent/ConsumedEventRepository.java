package com.miniblog.query.repository.consumedEvent;

import com.miniblog.query.model.ConsumedEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ConsumedEventRepository extends MongoRepository<ConsumedEvent, String>, ConsumedEventRepositoryCustom{
//    Optional<ConsumedEvent> findByProcessedFalseAndEventUuid(String eventUuid);
    Optional<ConsumedEvent> findByEventUuid(String eventUuid);
}
