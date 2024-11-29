package com.miniblog.post.repository.post;

import com.miniblog.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    Optional<Post> findByPostUuidAndUserUuid(UUID postUUid, UUID userUuid);
    void deleteByUserUuid(UUID userUuid);
}
