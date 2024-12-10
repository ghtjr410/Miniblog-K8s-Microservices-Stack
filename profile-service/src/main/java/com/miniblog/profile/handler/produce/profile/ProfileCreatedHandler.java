package com.miniblog.profile.handler.produce.profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniblog.profile.avro.ProfileCreatedEvent;
import com.miniblog.profile.handler.produce.AbstractEventProducerHandler;
import com.miniblog.profile.producer.EventProducer;
import com.miniblog.profile.tracing.SpanFactory;
import com.miniblog.profile.util.ProducedEventType;
import io.micrometer.tracing.Tracer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ProfileCreatedHandler extends AbstractEventProducerHandler<ProfileCreatedEvent> {

    @Value("${profile.created.event.topic.name}")
    private String profileCreatedTopicName;

    public ProfileCreatedHandler(EventProducer eventProducer,
                                 ObjectMapper objectMapper,
                                 Tracer tracer,
                                 SpanFactory spanFactory) {
        super(eventProducer, objectMapper, tracer, spanFactory, ProfileCreatedEvent.class, ProducedEventType.PROFILE_CREATED);
    }

    @PostConstruct
    public void init() {
        this.topicName = profileCreatedTopicName;
    }
}
