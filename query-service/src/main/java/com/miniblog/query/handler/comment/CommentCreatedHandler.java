package com.miniblog.query.handler.comment;

import com.miniblog.comment.avro.CommentCreatedEvent;
import com.miniblog.query.handler.EventConsumerHandler;
import com.miniblog.query.mapper.CommentMapper;
import com.miniblog.query.model.Comment;
import com.miniblog.query.repository.comment.CommentRepository;
import com.miniblog.query.repository.post.PostRepository;
import com.miniblog.query.util.ConsumedEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentCreatedHandler implements EventConsumerHandler {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;

    @Override
    public void handleEvent(SpecificRecordBase event) {
        CommentCreatedEvent commentCreatedEvent = (CommentCreatedEvent) event;
        String postUuid = commentCreatedEvent.getPostUuid().toString();
        String commentUuid = commentCreatedEvent.getCommentUuid().toString();

        if (commentRepository.existsById(commentUuid)) {
            log.info("이미 처리된 댓글 생성 이벤트: commentUuid={}", commentUuid);
            return;
        }

        Comment comment = commentMapper.toCreateComment(commentCreatedEvent);
        try {
            commentRepository.save(comment);
        } catch (DuplicateKeyException ex) {
            log.info("중복된 댓글 저장 시도: commentUuid={}, error={}", commentUuid, ex.getMessage());
            // 이미 저장된 경우 아무 작업도 하지 않음
        }

        boolean updated = postRepository.incrementCommentCount(postUuid);
        if (!updated) {
            throw new IllegalArgumentException("Post not found for postUuid: " + postUuid);
        }
    }

    @Override
    public ConsumedEventType getEventType() {
        return ConsumedEventType.COMMENT_CREATE;
    }
}
