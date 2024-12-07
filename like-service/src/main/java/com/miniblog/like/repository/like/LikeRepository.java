package com.miniblog.like.repository.like;

import com.miniblog.like.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostUuidAndUserUuid(UUID postUuid, UUID UserUuid);

    void deleteByUserUuid(UUID userUuid);
    void deleteByPostUuid(UUID postUuid);
}
