package com.miniblog.query.repository.consumedEvent;

import com.miniblog.query.helper.SagaStatusUpdater;
import com.miniblog.query.model.ConsumedEvent;
import com.miniblog.query.util.ConsumedEventType;
import com.miniblog.query.util.SagaStatus;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.Instant;

@RequiredArgsConstructor
public class ConsumedEventRepositoryImpl implements ConsumedEventRepositoryCustom{
    private final MongoOperations mongoOperations;
    private final SagaStatusUpdater sagaStatusUpdater;

    @Autowired
    public ConsumedEventRepositoryImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
        this.sagaStatusUpdater = new SagaStatusUpdater(mongoOperations);
    }

    @Override
    public boolean updateSagaStatus(String eventUuid, SagaStatus[] expectedCurrentStatus, SagaStatus newStatus, Boolean processed) {
        Query query = new Query(Criteria.where("eventUuid").is(eventUuid).and("sagaStatus").in((Object[])expectedCurrentStatus));
        Update update = new Update().set("sagaStatus", newStatus);
        if (processed != null) {
            update.set("processed", processed);
        }
        UpdateResult result = mongoOperations.updateFirst(query, update, ConsumedEvent.class);
        return result.getModifiedCount() > 0;
    }

    @Override
    public ConsumedEvent upsertEventToProcessing(String eventUuid, ConsumedEventType eventType, SagaStatus newStatus) {
        Query query = new Query(Criteria.where("eventUuid").is(eventUuid)
                .and("sagaStatus").in(SagaStatus.FAILED));

        Update update = new Update()
                .setOnInsert("eventUuid", eventUuid)
                .setOnInsert("eventType", eventType)
                .set("sagaStatus", newStatus)
                .setOnInsert("processed", false)
                .setOnInsert("createdDate", Instant.now());

        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(true);
        ConsumedEvent consumedEvent = mongoOperations.findAndModify(query, update, options, ConsumedEvent.class);

        // 상태가 PROCESSING이면 처리 시작 가능
        if (consumedEvent != null && consumedEvent.getSagaStatus() == SagaStatus.PROCESSING) {
            return consumedEvent;
        } else {
            return null;
        }
    }
}
