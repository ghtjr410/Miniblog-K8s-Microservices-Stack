package com.miniblog.comment.repository.comment;

import com.miniblog.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    void deleteByUserUuid(UUID userUuid);
    void deleteByPostUuid(UUID postUuid);
    Optional<Comment> findByCommentUuidAndUserUuid(String commentUuid, String userUuid);
}
