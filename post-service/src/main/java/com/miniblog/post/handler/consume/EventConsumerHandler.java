package com.miniblog.post.handler.consume;

import com.miniblog.post.util.ConsumedEventType;
import org.apache.avro.specific.SpecificRecordBase;

public interface EventConsumerHandler {
    void handleEvent(SpecificRecordBase event);

    ConsumedEventType getEventType();
}
