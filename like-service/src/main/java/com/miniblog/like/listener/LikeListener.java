package com.miniblog.like.listener;

import com.miniblog.like.model.Like;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
public class LikeListener {

    @PrePersist
    public void prePersist(Like like) {
        if (like.getCreatedDate() == null) {
            like.setCreatedDate(Instant.now());
        }
    }

    @PreRemove
    public void preRemove(Like like) {
        log.info("Like is about to be deleted = {}", like);
    }
}
