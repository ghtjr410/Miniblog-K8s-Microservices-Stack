package com.miniblog.viewcount.handler.produce;

import com.miniblog.viewcount.model.OutboxEvent;
import com.miniblog.viewcount.util.ProducedEventType;

public interface EventProducerHandler {
    void handleEvent(OutboxEvent outboxEvent) throws Exception;
    ProducedEventType getEventType();
}
