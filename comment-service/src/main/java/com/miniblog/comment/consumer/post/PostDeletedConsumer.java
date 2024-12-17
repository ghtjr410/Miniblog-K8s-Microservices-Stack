package com.miniblog.comment.consumer.post;

import com.miniblog.comment.consumer.GenericConsumer;
import com.miniblog.comment.service.consume.ConsumedEventProcessor;
import com.miniblog.comment.util.ConsumedEventType;
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
    public String topicName() {
        return topicName;
    }
}
