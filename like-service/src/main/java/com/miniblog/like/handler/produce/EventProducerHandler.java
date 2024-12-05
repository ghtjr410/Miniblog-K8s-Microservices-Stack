package com.miniblog.like.handler.produce;

import com.miniblog.like.model.OutboxEvent;
import com.miniblog.like.util.ProducedEventType;

public interface EventProducerHandler {
    void handleEvent(OutboxEvent outboxEvent) throws Exception;
    ProducedEventType getEventType();
}
