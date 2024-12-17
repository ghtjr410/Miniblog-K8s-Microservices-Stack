package com.miniblog.viewcount.consumer.post;

import com.miniblog.post.avro.PostDeletedEvent;
import com.miniblog.viewcount.consumer.GenericConsumer;
import com.miniblog.viewcount.service.consume.ConsumedEventProcessor;
import com.miniblog.viewcount.util.ConsumedEventType;
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
