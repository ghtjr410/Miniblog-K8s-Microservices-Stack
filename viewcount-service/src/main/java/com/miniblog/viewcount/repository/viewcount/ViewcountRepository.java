package com.miniblog.viewcount.repository.viewcount;

import com.miniblog.viewcount.model.Viewcount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ViewcountRepository extends JpaRepository<Viewcount, UUID> {
    void deleteByUserUuid(UUID userUuid);
}
