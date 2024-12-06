package com.miniblog.profile.listener;

import com.miniblog.profile.model.Profile;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProfileListener {

    @PrePersist
    public void prePersist(Profile profile) {
        log.info("Profile is about to be created = {}", profile);
    }

    @PreUpdate
    public void preUpdate(Profile profile) {
        log.info("Profile is about to be updated = {}", profile);
    }

    @PreRemove
    public void preRemove(Profile profile) {
        log.info("Profile is about to be deleted = {}", profile);
    }
}
