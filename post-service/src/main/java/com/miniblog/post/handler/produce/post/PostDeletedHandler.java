package com.miniblog.post.handler.produce.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniblog.post.avro.PostDeletedEvent;
import com.miniblog.post.handler.produce.AbstractEventProducerHandler;
import com.miniblog.post.producer.EventProducer;
import com.miniblog.post.tracing.SpanFactory;
import com.miniblog.post.util.ProducedEventType;
import io.micrometer.tracing.Tracer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PostDeletedHandler extends AbstractEventProducerHandler<PostDeletedEvent> {

    @Value("${post.deleted.event.topic.name}")
    private String postDeletedTopicName;

    public PostDeletedHandler(EventProducer eventProducer,
                              ObjectMapper objectMapper,
                              Tracer tracer,
                              SpanFactory spanFactory) {
        super(eventProducer, objectMapper, tracer, spanFactory, PostDeletedEvent.class, ProducedEventType.POST_DELETE);
    }

    @PostConstruct
    public void init() {
        this.topicName = postDeletedTopicName;
    }
}
