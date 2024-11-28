package com.miniblog.query.mapper;

import com.miniblog.like.avro.LikeCreatedEvent;
import com.miniblog.query.model.Like;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class LikeMapper {
    public Like toCreateLike(LikeCreatedEvent event) {
        return Like.builder()
                .likeUuid(event.getLikeUuid().toString())
                .postUuid(event.getPostUuid().toString())
                .userUuid(event.getUserUuid().toString())
                .createdDate(Instant.ofEpochMilli(event.getCreatedDate()))
                .build();
    }
}
