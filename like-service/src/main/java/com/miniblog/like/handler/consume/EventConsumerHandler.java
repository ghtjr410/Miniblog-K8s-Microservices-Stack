package com.miniblog.like.handler.consume;

import com.miniblog.like.util.ConsumedEventType;
import org.apache.avro.specific.SpecificRecordBase;

public interface EventConsumerHandler {
    void handleEvent(SpecificRecordBase event);
    ConsumedEventType getEventType();
}
