package com.miniblog.viewcount.handler.consume.account;

import com.miniblog.account.avro.AccountDeletedEvent;
import com.miniblog.viewcount.handler.consume.AbstractEventConsumerHandler;
import com.miniblog.viewcount.repository.viewcount.ViewcountRepository;
import com.miniblog.viewcount.util.ConsumedEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountDeletedEventHandler extends AbstractEventConsumerHandler<AccountDeletedEvent> {
    private final ViewcountRepository viewcountRepository;

    @Override
    protected void processEvent(SpecificRecordBase event) {
        AccountDeletedEvent accountDeletedEvent = (AccountDeletedEvent) event;
        UUID userUuid = UUID.fromString(accountDeletedEvent.getUserUuid().toString());
        viewcountRepository.deleteByUserUuid(userUuid);
    }

    @Override
    public ConsumedEventType getEventType() {
        return ConsumedEventType.ACCOUNT_DELETE;
    }
}