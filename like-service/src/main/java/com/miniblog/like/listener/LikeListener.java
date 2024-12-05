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
        Instant instant = Instant.now();
        if(like.getCreatedDate() == null) {
            like.setCreatedDate(instant);
        }
        log.info("Like is about to be created = {}", like);
    }

    @PreRemove
    public void preRemove(Like like) {
        log.info("Like is about to be deleted = {}", like);
    }
}