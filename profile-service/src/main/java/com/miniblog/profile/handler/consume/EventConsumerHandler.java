package com.miniblog.profile.handler.consume;

import com.miniblog.profile.util.ConsumedEventType;
import org.apache.avro.specific.SpecificRecordBase;

public interface EventConsumerHandler {
    void handleEvent(SpecificRecordBase event);
    ConsumedEventType getEventType();
}