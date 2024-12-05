package com.miniblog.like.mapper;

import com.miniblog.like.avro.LikeDeletedEvent;
import com.miniblog.like.model.Like;
import org.springframework.stereotype.Component;

@Component
public class LikeDeletedEventMapper {
    public LikeDeletedEvent toEntity(Like like) {
        LikeDeletedEvent likeDeletedEvent = new LikeDeletedEvent();
        likeDeletedEvent.setLikeUuid(like.getLikeUuid().toString());
        likeDeletedEvent.setPostUuid(like.getPostUuid().toString());
        return likeDeletedEvent;
    }
}
