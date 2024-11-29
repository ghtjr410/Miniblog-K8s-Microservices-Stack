package com.miniblog.post.handler.produce.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniblog.post.avro.PostCreatedEvent;
import com.miniblog.post.handler.produce.AbstractEventProducerHandler;
import com.miniblog.post.producer.EventProducer;
import com.miniblog.post.tracing.SpanFactory;
import com.miniblog.post.util.ProducedEventType;
import io.micrometer.tracing.Tracer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PostCreatedHandler extends AbstractEventProducerHandler<PostCreatedEvent> {

    @Value("${post.created.event.topic.name}")
    private String postCreatedTopicName;

    public PostCreatedHandler(EventProducer eventProducer,
                              ObjectMapper objectMapper,
                              Tracer tracer,
                              SpanFactory spanFactory) {
        super(eventProducer, objectMapper, tracer, spanFactory, PostCreatedEvent.class, ProducedEventType.POST_CREATE);
    }

    @PostConstruct
    public void init() {
        this.topicName = postCreatedTopicName;
    }
}
