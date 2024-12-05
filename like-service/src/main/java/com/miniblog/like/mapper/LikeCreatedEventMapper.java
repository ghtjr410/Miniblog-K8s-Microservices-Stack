package com.miniblog.like.mapper;

import com.miniblog.like.avro.LikeCreatedEvent;
import com.miniblog.like.model.Like;
import org.springframework.stereotype.Component;

@Component
public class LikeCreatedEventMapper {
    public LikeCreatedEvent toEntity(Like like) {
        LikeCreatedEvent likeCreatedEvent = new LikeCreatedEvent();
        likeCreatedEvent.setLikeUuid(like.getLikeUuid().toString());
        likeCreatedEvent.setPostUuid(like.getPostUuid().toString());
        likeCreatedEvent.setUserUuid(like.getUserUuid().toString());
        likeCreatedEvent.setCreatedDate(like.getCreatedDate().toEpochMilli());
        return likeCreatedEvent;
    }
}