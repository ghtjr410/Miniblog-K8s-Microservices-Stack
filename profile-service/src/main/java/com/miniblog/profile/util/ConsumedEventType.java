package com.miniblog.profile.util;

import com.miniblog.account.avro.AccountDeletedEvent;
import lombok.Getter;
import org.apache.avro.specific.SpecificRecordBase;

@Getter
public enum ConsumedEventType {
    ACCOUNT_DELETE(AccountDeletedEvent.class);

    private final Class<? extends SpecificRecordBase> eventClass;

    ConsumedEventType(Class<? extends SpecificRecordBase> eventClass) {
        this.eventClass = eventClass;
    }
}
