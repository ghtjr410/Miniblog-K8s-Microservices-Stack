package com.miniblog.post.mapper;

import com.miniblog.post.avro.PostCreatedEvent;
import com.miniblog.post.model.Post;
import org.springframework.stereotype.Component;

@Component
public class PostCreatedEventMapper {
    public PostCreatedEvent toEntity(Post post) {
        PostCreatedEvent postCreatedEvent = new PostCreatedEvent();
        postCreatedEvent.setPostUuid(post.getPostUuid().toString());
        postCreatedEvent.setUserUuid(post.getUserUuid().toString());
        postCreatedEvent.setNickname(post.getNickname());
        postCreatedEvent.setTitle(post.getTitle());
        postCreatedEvent.setContent(post.getContent());
        postCreatedEvent.setCreatedDate(post.getCreatedDate().toEpochMilli());
        postCreatedEvent.setUpdatedDate(post.getUpdatedDate().toEpochMilli());
        return postCreatedEvent;
    }
}
