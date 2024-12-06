package com.miniblog.viewcount.handler.consume;

import com.miniblog.viewcount.util.ConsumedEventType;
import org.apache.avro.specific.SpecificRecordBase;

public interface EventConsumerHandler {

    void handleEvent(SpecificRecordBase event);

    ConsumedEventType getEventType();
}
