package com.miniblog.account.handler;

import com.miniblog.account.model.OutboxEvent;
import com.miniblog.account.util.ProducedEventType;

public interface EventProducerHandler {
    void handleEvent(OutboxEvent outboxEvent) throws Exception;
    ProducedEventType getEventType();
}
