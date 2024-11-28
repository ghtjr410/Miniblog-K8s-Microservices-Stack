package com.miniblog.query.repository.like;

import com.miniblog.query.model.Like;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends MongoRepository<Like, String> {
    void deleteByPostUuid(String postUuid);
    void deleteByUserUuid(String userUuid);
    List<Like> findByUserUuid(String userUuid);
}
