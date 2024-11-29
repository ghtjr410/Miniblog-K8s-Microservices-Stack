package com.miniblog.post.listener;

import com.miniblog.post.model.Post;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
public class PostListener {

    @PrePersist
    public void prePersist(Post post) {
        Instant instant = Instant.now();
        if(post.getCreatedDate() == null) {
            post.setCreatedDate(instant);
        }
        if(post.getUpdatedDate() == null) {
            post.setUpdatedDate(instant);
        }
        log.info("Post is about to be created = {}", post);
    }

    @PreUpdate
    public void preUpdated(Post post) {
        post.setUpdatedDate(Instant.now());
        log.info("Post is about to be updated = {}", post);
    }

    @PreRemove
    public void preRemove(Post post) {
        log.info("Post is about to be deleted = {}", post);
    }
}
