package com.miniblog.post.handler.produce.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniblog.post.avro.PostUpdatedEvent;
import com.miniblog.post.handler.produce.AbstractEventProducerHandler;
import com.miniblog.post.producer.EventProducer;
import com.miniblog.post.tracing.SpanFactory;
import com.miniblog.post.util.ProducedEventType;
import io.micrometer.tracing.Tracer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PostUpdatedHandler extends AbstractEventProducerHandler<PostUpdatedEvent> {

    @Value("${post.updated.event.topic.name}")
    private String postUpdatedTopicName;

    public PostUpdatedHandler(EventProducer eventProducer,
                              ObjectMapper objectMapper,
                              Tracer tracer,
                              SpanFactory spanFactory) {
        super(eventProducer, objectMapper, tracer, spanFactory, PostUpdatedEvent.class, ProducedEventType.POST_UPDATE);
    }

    @PostConstruct
    public void init() {
        this.topicName = postUpdatedTopicName;
    }
}
