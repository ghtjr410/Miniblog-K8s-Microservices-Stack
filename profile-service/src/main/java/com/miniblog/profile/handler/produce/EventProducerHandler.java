package com.miniblog.profile.handler.produce;

import com.miniblog.profile.model.OutboxEvent;
import com.miniblog.profile.util.ProducedEventType;

public interface EventProducerHandler {
    void handleEvent(OutboxEvent outboxEvent) throws Exception;
    ProducedEventType getEventType();
}
