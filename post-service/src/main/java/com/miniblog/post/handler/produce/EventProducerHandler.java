package com.miniblog.post.handler.produce;

import com.miniblog.post.model.OutboxEvent;
import com.miniblog.post.util.ProducedEventType;

public interface EventProducerHandler {
    void handleEvent(OutboxEvent outboxEvent) throws Exception;
    ProducedEventType getEventType();
}
