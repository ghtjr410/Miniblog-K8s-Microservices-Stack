package com.miniblog.query.consumer.post;

import com.miniblog.post.avro.PostUpdatedEvent;
import com.miniblog.query.consumer.GenericConsumer;
import com.miniblog.query.service.ConsumedEventProcessor;
import com.miniblog.query.util.ConsumedEventType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PostUpdatedConsumer extends GenericConsumer<PostUpdatedEvent> {
    @Value("${post.updated.event.topic.name}")
    private String topicName;

    public PostUpdatedConsumer(ConsumedEventProcessor consumedEventProcessor) {
        super(consumedEventProcessor);
    }

    @Override
    protected ConsumedEventType getConsumedEventType() {
        return ConsumedEventType.POST_UPDATE;
    }

    @Override
    public String topicName() {
        return topicName;
    }
}
