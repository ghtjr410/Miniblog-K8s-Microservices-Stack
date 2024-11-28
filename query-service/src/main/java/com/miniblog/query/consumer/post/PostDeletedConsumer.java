package com.miniblog.query.consumer.post;

import com.miniblog.post.avro.PostDeletedEvent;
import com.miniblog.query.consumer.GenericConsumer;
import com.miniblog.query.service.ConsumedEventProcessor;
import com.miniblog.query.util.ConsumedEventType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PostDeletedConsumer extends GenericConsumer<PostDeletedEvent> {
    @Value("${post.deleted.event.topic.name}")
    private String topicName;

    public PostDeletedConsumer(ConsumedEventProcessor consumedEventProcessor) {
        super(consumedEventProcessor);
    }

    @Override
    protected ConsumedEventType getConsumedEventType() {
        return ConsumedEventType.POST_DELETE;
    }

    @Override
    public String topicName() {
        return topicName;
    }
}
