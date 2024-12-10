package com.miniblog.profile.handler.consume.account;

import com.miniblog.account.avro.AccountDeletedEvent;
import com.miniblog.profile.handler.consume.AbstractEventConsumerHandler;
import com.miniblog.profile.repository.profile.ProfileRepository;
import com.miniblog.profile.util.ConsumedEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountDeletedHandler extends AbstractEventConsumerHandler<AccountDeletedEvent> {
    private final ProfileRepository profileRepository;

    @Override
    public void processEvent(SpecificRecordBase event) {
        AccountDeletedEvent accountDeletedEvent = (AccountDeletedEvent) event;
        UUID userUuid = UUID.fromString(accountDeletedEvent.getUserUuid().toString());
        // 프로필 삭제
        profileRepository.deleteByUserUuid(userUuid);
    }

    @Override
    public ConsumedEventType getEventType() {
        return ConsumedEventType.ACCOUNT_DELETE;
    }
}
