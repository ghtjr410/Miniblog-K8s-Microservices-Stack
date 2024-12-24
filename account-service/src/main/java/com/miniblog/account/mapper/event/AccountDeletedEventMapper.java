package com.miniblog.account.mapper.event;

import com.miniblog.account.avro.AccountDeletedEvent;
import org.springframework.stereotype.Component;

@Component
public class AccountDeletedEventMapper {

    public AccountDeletedEvent createToEvent(String userUuid) {
        AccountDeletedEvent accountDeletedEvent = new AccountDeletedEvent();
        accountDeletedEvent.setUserUuid(userUuid);
        return accountDeletedEvent;
    }
}
