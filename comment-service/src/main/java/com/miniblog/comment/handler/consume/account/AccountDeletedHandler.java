package com.miniblog.comment.handler.consume.account;

import com.miniblog.account.avro.AccountDeletedEvent;
import com.miniblog.comment.handler.consume.AbstractEventConsumerHandler;
import com.miniblog.comment.repository.comment.CommentRepository;
import com.miniblog.comment.util.ConsumedEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountDeletedHandler extends AbstractEventConsumerHandler<AccountDeletedEvent> {
    private final CommentRepository commentRepository;

    @Override
    protected void processEvent(SpecificRecordBase event) {
        AccountDeletedEvent accountDeletedEvent = (AccountDeletedEvent) event;
        UUID userUuid = UUID.fromString(accountDeletedEvent.getUserUuid().toString());
        // 댓글 삭제
        commentRepository.deleteByUserUuid(userUuid);
    }

    @Override
    public ConsumedEventType getEventType() {
        return ConsumedEventType.ACCOUNT_DELETE;
    }
}
