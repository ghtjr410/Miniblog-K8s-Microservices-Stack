package com.miniblog.query.consumer.comment;

import com.miniblog.comment.avro.CommentUpdatedEvent;
import com.miniblog.query.consumer.GenericConsumer;
import com.miniblog.query.service.consumedEvent.ConsumedEventProcessor;
import com.miniblog.query.util.ConsumedEventType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CommentUpdatedConsumer extends GenericConsumer<CommentUpdatedEvent> {
    @Value("${comment.updated.event.topic.name}")
    private String topicName;

    public CommentUpdatedConsumer(ConsumedEventProcessor consumedEventProcessor) {
        super(consumedEventProcessor);
    }

    @Override
    protected ConsumedEventType getConsumedEventType() {
        return ConsumedEventType.COMMENT_UPDATE;
    }

    @Override
    public String topicName() {
        return topicName;
    }
}
