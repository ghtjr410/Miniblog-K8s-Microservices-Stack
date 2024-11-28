package com.miniblog.query.repository.comment;

import com.miniblog.query.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String>, CommentOperations {
    void deleteByPostUuid(String postUuid);
    void deleteByUserUuid(String userUuid);
    List<Comment> findByUserUuid(String userUuid);

    List<Comment> findByPostUuidOrderByCreatedDateAsc(String postUuid);

}
