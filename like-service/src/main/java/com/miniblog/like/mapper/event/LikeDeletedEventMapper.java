package com.miniblog.like.mapper.event;

import com.miniblog.like.avro.LikeDeletedEvent;
import com.miniblog.like.model.Like;
import org.springframework.stereotype.Component;

@Component
public class LikeDeletedEventMapper implements EventMapper {
    @Override
    public LikeDeletedEvent createToEvent(Like like) {
        LikeDeletedEvent likeDeletedEvent = new LikeDeletedEvent();
        likeDeletedEvent.setLikeUuid(like.getLikeUuid().toString());
        likeDeletedEvent.setPostUuid(like.getPostUuid().toString());
        return likeDeletedEvent;
    }
}
