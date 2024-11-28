package com.miniblog.query.repository.post;

import com.miniblog.query.model.Comment;
import com.miniblog.query.model.Post;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.Instant;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostIncrementOperations, PostDecrementOperations, PostUpdateOperations {
    private final MongoOperations mongoOperations;

    @Override
    public boolean incrementViewcount(String postUuid) {
        return modifyField(postUuid, "viewcount", 1);
    }

    @Override
    public boolean incrementLikeCount(String postUuid) {
        return modifyField(postUuid, "likeCount", 1);
    }

    @Override
    public boolean incrementCommentCount(String postUuid) {
        return modifyField(postUuid, "commentCount", 1);
    }

    @Override
    public boolean decrementLikeCount(String postUuid) {
        return modifyFieldWithLimit(postUuid, "likeCount", -1, 0);
    }

    @Override
    public boolean decrementCommentCount(String postUuid) {
        return modifyFieldWithLimit(postUuid, "commentCount", -1, 0);
    }

    @Override
    public boolean updatePost(String postUuid, String title, String content, Instant updatedDate) {
        Query query = new Query(Criteria.where("postUuid").is(postUuid));
        Update update = new Update()
                .set("title", title)
                .set("content", content)
                .set("updatedDate", updatedDate);
        UpdateResult result = mongoOperations.updateFirst(query, update, Post.class);
        return result.getModifiedCount() > 0;
    }

    private boolean modifyField(String postUuid, String fieldName, int delta) {
        Query query = new Query(Criteria.where("postUuid").is(postUuid));
        Update update = new Update().inc(fieldName, delta);
        UpdateResult result = mongoOperations.updateFirst(query, update, Post.class);
        return result.getModifiedCount() > 0;
    }

    private boolean modifyFieldWithLimit(String postUuid, String fieldName, int delta, int minValue) {
        Query query = new Query(Criteria.where("postUuid").is(postUuid).and(fieldName).gt(minValue - delta));
        Update update = new Update().inc(fieldName, delta);
        UpdateResult result = mongoOperations.updateFirst(query, update, Post.class);
        return result.getModifiedCount() > 0;
    }
}
