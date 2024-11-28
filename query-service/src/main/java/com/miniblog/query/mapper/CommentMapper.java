package com.miniblog.query.mapper;

import com.miniblog.comment.avro.CommentCreatedEvent;
import com.miniblog.query.model.Comment;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class CommentMapper {
    public Comment toCreateComment(CommentCreatedEvent event) {
        return Comment.builder()
                .commentUuid(event.getCommentUuid().toString())
                .postUuid(event.getPostUuid().toString())
                .userUuid(event.getUserUuid().toString())
                .nickname(event.getNickname().toString())
                .content(event.getContent().toString())
                .createdDate(Instant.ofEpochMilli(event.getCreatedDate()))
                .updatedDate(Instant.ofEpochMilli(event.getUpdatedDate()))
                .build();
    }
}
