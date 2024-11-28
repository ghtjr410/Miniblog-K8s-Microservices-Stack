package com.miniblog.query.consumer.like;

import com.miniblog.like.avro.LikeCreatedEvent;
import com.miniblog.query.consumer.GenericConsumer;
import com.miniblog.query.service.consumedEvent.ConsumedEventProcessor;
import com.miniblog.query.util.ConsumedEventType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LikeCreatedConsumer extends GenericConsumer<LikeCreatedEvent> {
    @Value("${like.created.event.topic.name}")
    private String topicName;

    public LikeCreatedConsumer(ConsumedEventProcessor consumedEventProcessor) {
        super(consumedEventProcessor);
    }

    @Override
    protected ConsumedEventType getConsumedEventType() {
        return ConsumedEventType.COMMENT_CREATE;
    }

    @Override
    public String topicName() {
        return topicName;
    }
}
