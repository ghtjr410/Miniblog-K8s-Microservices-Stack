package com.miniblog.comment.mapper.event;

import com.miniblog.comment.avro.CommentUpdatedEvent;
import com.miniblog.comment.model.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentUpdatedEventMapper implements EventMapper {
    public CommentUpdatedEvent createToEvent(Comment comment) {
        CommentUpdatedEvent commentUpdatedEvent = new CommentUpdatedEvent();
        commentUpdatedEvent.setCommentUuid(comment.getCommentUuid().toString());
        commentUpdatedEvent.setContent(comment.getContent());
        commentUpdatedEvent.setUpdatedDate(comment.getUpdatedDate().toEpochMilli());
        return commentUpdatedEvent;
    }
}
