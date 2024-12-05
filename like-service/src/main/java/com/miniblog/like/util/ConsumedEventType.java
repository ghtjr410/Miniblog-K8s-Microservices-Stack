package com.miniblog.like.util;

import com.miniblog.account.avro.AccountDeletedEvent;
import com.miniblog.post.avro.PostDeletedEvent;
import lombok.Getter;
import org.apache.avro.specific.SpecificRecordBase;

@Getter
public enum ConsumedEventType {
    ACCOUNT_DELETE(AccountDeletedEvent.class),
    POST_DELETE(PostDeletedEvent.class);

    private final Class<? extends SpecificRecordBase> eventClass;

    ConsumedEventType(Class<? extends SpecificRecordBase> eventClass) {
        this.eventClass = eventClass;
    }
}