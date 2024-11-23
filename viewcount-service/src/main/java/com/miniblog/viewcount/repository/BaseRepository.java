package com.miniblog.viewcount.repository;

import com.miniblog.viewcount.util.SagaStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID> {

    boolean updateSagaStatus(UUID eventUuid, SagaStatus[] expectedCurrentStatus, SagaStatus newStatus, Boolean processed);
}
