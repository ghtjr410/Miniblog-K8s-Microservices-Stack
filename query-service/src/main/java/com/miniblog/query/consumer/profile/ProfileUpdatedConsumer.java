package com.miniblog.query.consumer.profile;

import com.miniblog.profile.avro.ProfileUpdatedEvent;
import com.miniblog.query.consumer.GenericConsumer;
import com.miniblog.query.service.ConsumedEventProcessor;
import com.miniblog.query.util.ConsumedEventType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProfileUpdatedConsumer extends GenericConsumer<ProfileUpdatedEvent> {
    @Value("${profile.updated.event.topic.name}")
    private String topicName;

    public ProfileUpdatedConsumer(ConsumedEventProcessor consumedEventProcessor) {
        super(consumedEventProcessor);
    }

    @Override
    protected ConsumedEventType getConsumedEventType() {
        return ConsumedEventType.PROFILE_UPDATE;
    }

    @Override
    public String topicName() {
        return topicName;
    }
}
