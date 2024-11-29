package com.miniblog.post.mapper;

import com.miniblog.post.avro.PostUpdatedEvent;
import com.miniblog.post.model.Post;
import org.springframework.stereotype.Component;

@Component
public class PostUpdatedEventMapper {
    public PostUpdatedEvent toEntity(Post post) {
        PostUpdatedEvent postUpdatedEvent = new PostUpdatedEvent();
        postUpdatedEvent.setPostUuid(post.getPostUuid().toString());
        postUpdatedEvent.setTitle(post.getTitle());
        postUpdatedEvent.setContent(post.getContent());
        postUpdatedEvent.setUpdatedDate(post.getUpdatedDate().toEpochMilli());
        return postUpdatedEvent;
    }
}
