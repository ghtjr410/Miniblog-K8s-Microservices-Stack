package com.miniblog.query.consumer.comment;

import com.miniblog.comment.avro.CommentDeletedEvent;
import com.miniblog.query.consumer.GenericConsumer;
import com.miniblog.query.service.consumedEvent.ConsumedEventProcessor;
import com.miniblog.query.util.ConsumedEventType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CommentDeletedConsumer extends GenericConsumer<CommentDeletedEvent> {
    @Value("${comment.deleted.event.topic.name}")
    private String topicName;

    public CommentDeletedConsumer(ConsumedEventProcessor consumedEventProcessor) {
        super(consumedEventProcessor);
    }

    @Override
    protected ConsumedEventType getConsumedEventType() {
        return ConsumedEventType.COMMENT_DELETE;
    }

    @Override
    public String topicName(){
        return topicName;
    }
}
