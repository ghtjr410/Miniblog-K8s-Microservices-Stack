package com.miniblog.viewcount.consumer.post;

import com.miniblog.post.avro.PostCreatedEvent;
import com.miniblog.viewcount.consumer.GenericConsumer;
import com.miniblog.viewcount.service.consume.ConsumedEventProcessor;
import com.miniblog.viewcount.util.ConsumedEventType;
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
    protected String topicName() {
        return topicName;
    }
}
