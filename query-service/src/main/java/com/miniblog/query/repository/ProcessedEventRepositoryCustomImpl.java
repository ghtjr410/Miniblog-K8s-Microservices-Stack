package com.miniblog.query.repository;

import com.miniblog.query.util.SagaStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProcessedEventRepositoryCustomImpl implements ProcessedEventRepositoryCustom {
    private final MongoTemplate mongoTemplate;

    @Override
    public boolean updateSagaStatus(String eventUuid, SagaStatus[] expectedCurrentStatus, SagaStatus newStatus) {
        Query query = new Query(Criteria.where("eventUuid").is(eventUuid)
                .and("sagaStatus").in(expectedCurrentStatus));
        Update update = new Update().set("sagaStatus", newStatus);
        return mongoTemplate.updateFirst(query, update, "processed_event").getModifiedCount() > 0;
    }
}
