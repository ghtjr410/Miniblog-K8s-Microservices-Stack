package com.miniblog.query.handler;

import com.miniblog.query.util.ConsumedEventType;
import org.apache.avro.specific.SpecificRecordBase;

public interface EventConsumerHandler {
    void handleEvent(SpecificRecordBase event);

    ConsumedEventType getEventType();
}
