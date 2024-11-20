package com.miniblog.viewcount.repository;

import com.miniblog.viewcount.model.Viewcount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ViewcountRepository extends JpaRepository<Viewcount, UUID> {
    @Modifying
    @Query("UPDATE Viewcount v SET v.totalViews = v.totalViews + 1 WHERE v.postUuid = :postUuid")
    int incrementViewcount(@Param("postUuid") UUID postUuid);

    @Query("SELECT v.totalViews FROM Viewcount v WHERE v.postUuid = :postUuid")
    Long findTotalViewsByPostUuid(@Param("postUuid") UUID postUuid);
}
