package com.miniblog.query.consumer.post;

import com.miniblog.post.avro.PostCreatedEvent;
import com.miniblog.query.consumer.GenericConsumer;
import com.miniblog.query.service.consumedEvent.ConsumedEventProcessor;
import com.miniblog.query.util.ConsumedEventType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PostCreatedConsumer extends GenericConsumer<PostCreatedEvent> {
    @Value("${post.created.event.topic.name}")
    private String topicName;

    public PostCreatedConsumer(ConsumedEventProcessor consumedEventProcessor) {
        super(consumedEventProcessor);
    }

    @Override
    protected ConsumedEventType getConsumedEventType() {
        return ConsumedEventType.POST_CREATE;
    }

    @Override
    public String topicName() {
        return topicName;
    }
}
