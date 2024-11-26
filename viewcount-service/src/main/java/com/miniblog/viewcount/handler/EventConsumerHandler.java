package com.miniblog.viewcount.handler;

import com.miniblog.viewcount.util.ConsumedEventType;
import org.apache.avro.specific.SpecificRecordBase;

import java.util.UUID;

public interface EventConsumerHandler {

    void handleEvent(UUID eventUuid, SpecificRecordBase event);

    ConsumedEventType getEventType();
}
