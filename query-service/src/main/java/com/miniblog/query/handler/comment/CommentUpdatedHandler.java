package com.miniblog.query.handler.comment;

import com.miniblog.comment.avro.CommentUpdatedEvent;
import com.miniblog.query.handler.EventConsumerHandler;
import com.miniblog.query.repository.comment.CommentRepository;
import com.miniblog.query.util.ConsumedEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentUpdatedHandler implements EventConsumerHandler {
    private final CommentRepository commentRepository;

    @Override
    public void handleEvent(SpecificRecordBase event) {
        CommentUpdatedEvent commentUpdatedEvent = (CommentUpdatedEvent) event;
        String commentUuid = commentUpdatedEvent.getCommentUuid().toString();
        String content = commentUpdatedEvent.getContent().toString();
        Instant updatedDate = Instant.ofEpochMilli(commentUpdatedEvent.getUpdatedDate());

        commentRepository.updateComment(commentUuid, content, updatedDate);
    }

    @Override
    public ConsumedEventType getEventType() {
        return ConsumedEventType.COMMENT_UPDATE;
    }
}
