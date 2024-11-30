package com.miniblog.comment.handler.produce.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniblog.comment.avro.CommentDeletedEvent;
import com.miniblog.comment.handler.produce.AbstractEventProducerHandler;
import com.miniblog.comment.producer.EventProducer;
import com.miniblog.comment.tracing.SpanFactory;
import com.miniblog.comment.util.ProducedEventType;
import io.micrometer.tracing.Tracer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CommentDeletedHandler extends AbstractEventProducerHandler<CommentDeletedEvent> {

    @Value("${comment.deleted.event.topic.name}")
    private String commentDeletedTopicName;

    public CommentDeletedHandler(EventProducer eventProducer,
                                 ObjectMapper objectMapper,
                                 Tracer tracer,
                                 SpanFactory spanFactory) {
        super(eventProducer, objectMapper, tracer, spanFactory, CommentDeletedEvent.class, ProducedEventType.COMMENT_DELETED);
    }

    @PostConstruct
    public void init() {
        this.topicName = commentDeletedTopicName;
    }
}
