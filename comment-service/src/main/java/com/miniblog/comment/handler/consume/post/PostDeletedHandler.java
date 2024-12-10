package com.miniblog.comment.handler.consume.post;

import com.miniblog.comment.handler.consume.AbstractEventConsumerHandler;
import com.miniblog.comment.repository.comment.CommentRepository;
import com.miniblog.comment.util.ConsumedEventType;
import com.miniblog.post.avro.PostDeletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostDeletedHandler extends AbstractEventConsumerHandler<PostDeletedEvent> {
    private final CommentRepository commentRepository;

    @Override
    public void processEvent(SpecificRecordBase event) {
        PostDeletedEvent postDeletedEvent = (PostDeletedEvent) event;
        UUID postUuid = UUID.fromString(postDeletedEvent.getPostUuid().toString());
        // 댓글 삭제
        commentRepository.deleteByPostUuid(postUuid);
    }

    @Override
    public ConsumedEventType getEventType() {
        return ConsumedEventType.POST_DELETE;
    }
}
