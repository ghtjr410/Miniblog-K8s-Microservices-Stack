package com.miniblog.query.consumer.like;

import com.miniblog.like.avro.LikeDeletedEvent;
import com.miniblog.query.consumer.GenericConsumer;
import com.miniblog.query.service.ConsumedEventProcessor;
import com.miniblog.query.util.ConsumedEventType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LikeDeletedConsumer extends GenericConsumer<LikeDeletedEvent> {
    @Value("${like.deleted.event.topic.name}")
    private String topicName;

    public LikeDeletedConsumer(ConsumedEventProcessor consumedEventProcessor) {
        super(consumedEventProcessor);
    }

    @Override
    protected ConsumedEventType getConsumedEventType(){
        return ConsumedEventType.LIKE_DELETE;
    }

    @Override
    public String topicName() {
        return topicName;
    }
}
