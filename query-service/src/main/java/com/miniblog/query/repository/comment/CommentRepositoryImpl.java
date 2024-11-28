package com.miniblog.query.repository.comment;

import com.miniblog.query.model.Comment;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.Instant;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentOperations{
    private final MongoOperations mongoOperations;

    @Override
    public boolean updateComment(String commentUuid, String content, Instant updatedDate) {
        Query query = new Query(Criteria.where("commentUuid").is(commentUuid));
        Update update = new Update()
                .set("content", content)
                .set("updatedDate", updatedDate);
        UpdateResult result = mongoOperations.updateFirst(query, update, Comment.class);
        return result.getModifiedCount() > 0;
    }
}
