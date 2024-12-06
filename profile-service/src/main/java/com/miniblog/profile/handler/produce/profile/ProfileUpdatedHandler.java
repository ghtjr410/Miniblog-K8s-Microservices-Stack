package com.miniblog.profile.handler.produce.profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniblog.profile.avro.ProfileUpdatedEvent;
import com.miniblog.profile.handler.produce.AbstractEventProducerHandler;
import com.miniblog.profile.producer.EventProducer;
import com.miniblog.profile.tracing.SpanFactory;
import com.miniblog.profile.util.ProducedEventType;
import io.micrometer.tracing.Tracer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProfileUpdatedHandler extends AbstractEventProducerHandler<ProfileUpdatedEvent> {
    @Value("${profile.updated.event.topic.name}")
    private String profileUpdatedTopicName;

    public ProfileUpdatedHandler(EventProducer eventProducer,
                                 ObjectMapper objectMapper,
                                 Tracer tracer,
                                 SpanFactory spanFactory) {
        super(eventProducer, objectMapper, tracer, spanFactory, ProfileUpdatedEvent.class, ProducedEventType.PROFILE_UPDATED);
    }

    @PostConstruct
    public void init() {
        this.topicName = profileUpdatedTopicName;
    }
}
