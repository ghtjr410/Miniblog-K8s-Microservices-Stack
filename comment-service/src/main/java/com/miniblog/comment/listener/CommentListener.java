package com.miniblog.comment.listener;

import com.miniblog.comment.model.Comment;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
public class CommentListener {

    @PrePersist
    public void prePersist(Comment comment) {
        Instant instant = Instant.now();
        if (comment.getCreatedDate() == null) {
            comment.setCreatedDate(instant);
        }
        if (comment.getUpdatedDate() == null) {
            comment.setUpdatedDate(instant);
        }
        log.info("Comment is about to be created = {}", comment);
    }

    @PreUpdate
    public void preUpdated(Comment comment) {
        comment.setUpdatedDate(Instant.now());
        log.info("Comment is about to be updated = {}", comment);
    }

    @PreRemove
    public void preRemove(Comment comment) {
        log.info("Comment is about to be deleted = {}", comment);
    }
}
