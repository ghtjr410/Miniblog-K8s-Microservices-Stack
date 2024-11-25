package com.miniblog.viewcount.listener;

import com.miniblog.viewcount.model.Viewcount;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ViewcountListener {

    @PrePersist
    public void prePersist(Viewcount viewcount) {
        if (viewcount.getTotalViews() == null) {
            viewcount.setTotalViews(0L);
        }
        log.info("viewcount is about to be created = {}", viewcount);
    }

    @PreUpdate
    public void preUpdate(Viewcount viewcount) {
        log.info("viewcount is about to be updated = {}", viewcount);
    }

    @PreRemove
    public void preRemove(Viewcount viewcount) {
        log.info("viewcount is about to be deleted = {}", viewcount);
    }
}
