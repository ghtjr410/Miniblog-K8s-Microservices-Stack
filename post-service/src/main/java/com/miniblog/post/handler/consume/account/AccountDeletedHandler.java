package com.miniblog.post.handler.consume.account;

import com.miniblog.account.avro.AccountDeletedEvent;
import com.miniblog.post.handler.consume.AbstractEventConsumerHandler;
import com.miniblog.post.repository.post.PostRepository;
import com.miniblog.post.util.ConsumedEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountDeletedHandler extends AbstractEventConsumerHandler<AccountDeletedEvent> {
    private final PostRepository postRepository;

    @Override
    protected void processEvent(SpecificRecordBase event) {
        AccountDeletedEvent accountDeletedEvent = (AccountDeletedEvent) event;
        UUID userUuid = UUID.fromString(accountDeletedEvent.getUserUuid().toString());
        // 게시글 삭제
        postRepository.deleteByUserUuid(userUuid);
    }

    @Override
    public ConsumedEventType getEventType() {
        return ConsumedEventType.ACCOUNT_DELETE;
    }
}