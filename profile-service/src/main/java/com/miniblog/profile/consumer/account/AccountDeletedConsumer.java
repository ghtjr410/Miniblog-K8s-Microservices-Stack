package com.miniblog.profile.consumer.account;

import com.miniblog.account.avro.AccountDeletedEvent;
import com.miniblog.profile.consumer.GenericConsumer;
import com.miniblog.profile.service.consume.ConsumedEventProcessor;
import com.miniblog.profile.util.ConsumedEventType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AccountDeletedConsumer extends GenericConsumer<AccountDeletedEvent> {
    @Value("${account.deleted.event.topic.name}")
    private String topicName;

    public AccountDeletedConsumer(ConsumedEventProcessor consumedEventProcessor) {
        super(consumedEventProcessor);
    }

    @Override
    protected ConsumedEventType getConsumedEventType() {
        return ConsumedEventType.ACCOUNT_DELETE;
    }

    @Override
    public String topicName() {
        return topicName;
    }
}
