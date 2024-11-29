package com.miniblog.post.mapper;

import com.miniblog.post.avro.PostDeletedEvent;
import com.miniblog.post.model.Post;
import org.springframework.stereotype.Component;

@Component
public class PostDeletedEventMapper {
    public PostDeletedEvent toEntity(Post post) {
        PostDeletedEvent postDeletedEvent = new PostDeletedEvent();
        postDeletedEvent.setPostUuid(post.getPostUuid().toString());
        return postDeletedEvent;
    }
}
