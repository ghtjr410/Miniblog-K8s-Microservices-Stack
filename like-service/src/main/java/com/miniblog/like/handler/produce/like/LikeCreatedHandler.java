package com.miniblog.like.handler.produce.like;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniblog.like.avro.LikeCreatedEvent;
import com.miniblog.like.handler.produce.AbstractEventProducerHandler;
import com.miniblog.like.producer.EventProducer;
import com.miniblog.like.tracing.SpanFactory;
import com.miniblog.like.util.ProducedEventType;
import io.micrometer.tracing.Tracer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LikeCreatedHandler extends AbstractEventProducerHandler<LikeCreatedEvent> {

    @Value("${like.created.event.topic.name}")
    private String likeCreatedTopicName;

    public LikeCreatedHandler(EventProducer eventProducer,
                              ObjectMapper objectMapper,
                              Tracer tracer,
                              SpanFactory spanFactory) {
        super(eventProducer, objectMapper, tracer, spanFactory, LikeCreatedEvent.class, ProducedEventType.LIKE_CREATED);
    }

    @PostConstruct
    public void init() {
        this.topicName = likeCreatedTopicName;
    }
}
