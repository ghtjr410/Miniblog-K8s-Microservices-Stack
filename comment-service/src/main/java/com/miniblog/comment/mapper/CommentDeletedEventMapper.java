package com.miniblog.comment.mapper;

import com.miniblog.comment.avro.CommentDeletedEvent;
import com.miniblog.comment.model.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentDeletedEventMapper {
    public CommentDeletedEvent toEntity(Comment comment) {
        CommentDeletedEvent commentDeletedEvent = new CommentDeletedEvent();
        commentDeletedEvent.setCommentUuid(comment.getCommentUuid().toString());
        commentDeletedEvent.setPostUuid(comment.getPostUuid().toString());
        return commentDeletedEvent;
    }
}
