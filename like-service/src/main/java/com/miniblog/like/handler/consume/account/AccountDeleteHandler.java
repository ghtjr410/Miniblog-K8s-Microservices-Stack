package com.miniblog.like.handler.consume.account;

import com.miniblog.account.avro.AccountDeletedEvent;
import com.miniblog.like.handler.consume.AbstractEventConsumerHandler;
import com.miniblog.like.repository.like.LikeRepository;
import com.miniblog.like.util.ConsumedEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountDeleteHandler extends AbstractEventConsumerHandler<AccountDeletedEvent> {
    private final LikeRepository likeRepository;

    @Override
    protected void processEvent(SpecificRecordBase event) {
        AccountDeletedEvent accountDeletedEvent = (AccountDeletedEvent) event;
        UUID userUuid = UUID.fromString(accountDeletedEvent.getUserUuid().toString());
        // 좋아요 삭제
        likeRepository.deleteByUserUuid(userUuid);
    }

    @Override
    public ConsumedEventType getEventType() {
        return ConsumedEventType.ACCOUNT_DELETE;
    }
}
