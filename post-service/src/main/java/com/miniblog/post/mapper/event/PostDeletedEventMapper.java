package com.miniblog.post.mapper.event;

import com.miniblog.post.avro.PostDeletedEvent;
import com.miniblog.post.model.Post;
import org.springframework.stereotype.Component;

@Component
public class PostDeletedEventMapper implements EventMapper {

    @Override
    public PostDeletedEvent createToEvent(Post post) {
        PostDeletedEvent postDeletedEvent = new PostDeletedEvent();
        postDeletedEvent.setPostUuid(post.getPostUuid().toString());
        return postDeletedEvent;
    }
}
