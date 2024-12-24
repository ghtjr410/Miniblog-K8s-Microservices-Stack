package com.miniblog.query.consumer.profile;


import com.miniblog.profile.avro.ProfileCreatedEvent;
import com.miniblog.query.consumer.GenericConsumer;
import com.miniblog.query.service.consumedEvent.ConsumedEventProcessor;
import com.miniblog.query.util.ConsumedEventType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProfileCreatedConsumer extends GenericConsumer<ProfileCreatedEvent> {
    @Value("${profile.created.event.topic.name}")
    private String topicName;

    public ProfileCreatedConsumer(ConsumedEventProcessor consumedEventProcessor) {
        super(consumedEventProcessor);
    }

    @Override
    protected ConsumedEventType getConsumedEventType() {
        return ConsumedEventType.PROFILE_CREATE;
    }

    @Override
    public String topicName() {
        return topicName;
    }
}
