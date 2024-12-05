package com.miniblog.like.consumer.post;

import com.miniblog.like.consumer.GenericConsumer;
import com.miniblog.like.service.consume.ConsumedEventProcessor;
import com.miniblog.like.util.ConsumedEventType;
import com.miniblog.post.avro.PostDeletedEvent;
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
    protected String topicName() {
        return topicName;
    }
}
