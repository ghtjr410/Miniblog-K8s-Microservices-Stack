package com.miniblog.comment.handler.produce.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniblog.comment.avro.CommentUpdatedEvent;
import com.miniblog.comment.handler.produce.AbstractEventProducerHandler;
import com.miniblog.comment.producer.EventProducer;
import com.miniblog.comment.tracing.SpanFactory;
import com.miniblog.comment.util.ProducedEventType;
import io.micrometer.tracing.Tracer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CommentUpdatedHandler extends AbstractEventProducerHandler<CommentUpdatedEvent> {

    @Value("${comment.updated.event.topic.name}")
    private String commentUpdatedTopicName;

    public CommentUpdatedHandler(EventProducer eventProducer,
                                 ObjectMapper objectMapper,
                                 Tracer tracer,
                                 SpanFactory spanFactory) {
        super(eventProducer, objectMapper, tracer, spanFactory, CommentUpdatedEvent.class, ProducedEventType.COMMENT_UPDATED);
    }

    @PostConstruct
    public void init() {
        this.topicName = commentUpdatedTopicName;
    }
}
