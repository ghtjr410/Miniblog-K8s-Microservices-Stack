package com.miniblog.comment.mapper.event;

import com.miniblog.comment.avro.CommentCreatedEvent;
import com.miniblog.comment.model.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentCreatedEventMapper implements EventMapper {
    public CommentCreatedEvent createToEvent(Comment comment) {
        CommentCreatedEvent commentCreatedEvent = new CommentCreatedEvent();
        commentCreatedEvent.setCommentUuid(comment.getCommentUuid().toString());
        commentCreatedEvent.setPostUuid(comment.getPostUuid().toString());
        commentCreatedEvent.setUserUuid(comment.getUserUuid().toString());
        commentCreatedEvent.setNickname(comment.getNickname());
        commentCreatedEvent.setContent(comment.getContent());
        commentCreatedEvent.setCreatedDate(comment.getCreatedDate().toEpochMilli());
        commentCreatedEvent.setUpdatedDate(comment.getUpdatedDate().toEpochMilli());
        return commentCreatedEvent;
    }
}
