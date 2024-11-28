package com.miniblog.query.handler.comment;

import com.miniblog.comment.avro.CommentDeletedEvent;
import com.miniblog.query.handler.EventConsumerHandler;
import com.miniblog.query.repository.comment.CommentRepository;
import com.miniblog.query.repository.post.PostRepository;
import com.miniblog.query.util.ConsumedEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentDeletedHandler implements EventConsumerHandler {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Override
    public void handleEvent(SpecificRecordBase event) {
        CommentDeletedEvent commentDeletedEvent = (CommentDeletedEvent) event;
        String postUuid = commentDeletedEvent.getPostUuid().toString();
        String commentUuid = commentDeletedEvent.getCommentUuid().toString();

        commentRepository.deleteById(commentUuid);

        boolean updated = postRepository.decrementCommentCount(postUuid);
        if (!updated) {
            throw new IllegalArgumentException("Post not found for postUuid: " + postUuid);
        }
    }

    @Override
    public ConsumedEventType getEventType() {
        return ConsumedEventType.COMMENT_DELETE;
    }
}
