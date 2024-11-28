package com.miniblog.query.consumer.comment;

import com.miniblog.comment.avro.CommentCreatedEvent;
import com.miniblog.query.consumer.GenericConsumer;
import com.miniblog.query.service.ConsumedEventProcessor;
import com.miniblog.query.util.ConsumedEventType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CommentCreatedConsumer extends GenericConsumer<CommentCreatedEvent> {
    @Value("${comment.created.event.topic.name}")
    private String topicName;

    public CommentCreatedConsumer(ConsumedEventProcessor consumedEventProcessor) {
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
