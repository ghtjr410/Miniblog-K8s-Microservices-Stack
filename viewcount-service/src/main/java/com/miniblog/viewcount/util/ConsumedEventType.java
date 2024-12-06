package com.miniblog.viewcount.util;

import com.miniblog.account.avro.AccountDeletedEvent;
import com.miniblog.post.avro.PostCreatedEvent;
import com.miniblog.post.avro.PostDeletedEvent;
import lombok.Getter;
import org.apache.avro.specific.SpecificRecordBase;

@Getter
public enum ConsumedEventType {
    ACCOUNT_DELETE(AccountDeletedEvent.class),
    POST_CREATE(PostCreatedEvent.class),
    POST_DELETE(PostDeletedEvent.class);

    private final Class<? extends SpecificRecordBase> eventClass;

    ConsumedEventType(Class<? extends SpecificRecordBase> eventClass) {
        this.eventClass = eventClass;
    }
}
