package com.miniblog.query.repository;

import com.miniblog.query.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    Post findByPostUuid(String postUuid);
}
