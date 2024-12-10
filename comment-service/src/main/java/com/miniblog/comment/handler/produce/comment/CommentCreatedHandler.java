package com.miniblog.comment.handler.produce.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniblog.comment.avro.CommentCreatedEvent;
import com.miniblog.comment.handler.produce.AbstractEventProducerHandler;
import com.miniblog.comment.producer.EventProducer;
import com.miniblog.comment.tracing.SpanFactory;
import com.miniblog.comment.util.ProducedEventType;
import io.micrometer.tracing.Tracer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CommentCreatedHandler extends AbstractEventProducerHandler<CommentCreatedEvent> {
    @Value("${comment.created.event.topic.name}")
    private String commentCreatedTopicName;

    public CommentCreatedHandler(EventProducer eventProducer,
                                 ObjectMapper objectMapper,
                                 Tracer tracer,
                                 SpanFactory spanFactory) {
        super(eventProducer, objectMapper, tracer, spanFactory, CommentCreatedEvent.class, ProducedEventType.COMMENT_CREATED);
    }

    @PostConstruct
    public void init() {
        this.topicName = commentCreatedTopicName;
    }
}
