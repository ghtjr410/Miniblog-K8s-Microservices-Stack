package com.miniblog.comment.handler.produce;

import com.miniblog.comment.model.OutboxEvent;
import com.miniblog.comment.util.ProducedEventType;

public interface EventProducerHandler {
    void handleEvent(OutboxEvent outboxEvent) throws Exception;
    ProducedEventType getEventType();
}
