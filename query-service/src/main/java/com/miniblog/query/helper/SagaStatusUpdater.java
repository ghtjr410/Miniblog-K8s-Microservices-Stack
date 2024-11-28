package com.miniblog.query.helper;

import com.miniblog.query.util.SagaStatus;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@RequiredArgsConstructor
public class SagaStatusUpdater {
    private final MongoOperations mongoOperations;

    public boolean updateSagaStatus(String eventUuid, SagaStatus[] expectedCurrentStatus, SagaStatus newStatus, Boolean processed, Class<?> domainClass) {
        Query query = new Query(Criteria.where("eventUuid").is(eventUuid).and("sagaStatus").in((Object[])expectedCurrentStatus));
        Update update = new Update().set("sagaStatus", newStatus);
        if (processed != null) {
            update.set("processed", processed);
        }
        UpdateResult result = mongoOperations.updateFirst(query, update, domainClass);
        return result.getModifiedCount() > 0;
    }
}
