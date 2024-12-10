package com.miniblog.comment.handler.consume;

import com.miniblog.comment.util.ConsumedEventType;
import org.apache.avro.specific.SpecificRecordBase;

public interface EventConsumerHandler {
    void handleEvent(SpecificRecordBase event);
    ConsumedEventType getEventType();
}
