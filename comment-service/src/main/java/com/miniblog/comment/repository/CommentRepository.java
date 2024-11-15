package com.miniblog.comment.repository;

import com.miniblog.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByCommentUuidAndUserUuid(String commentUuid, String userUuid);
}
