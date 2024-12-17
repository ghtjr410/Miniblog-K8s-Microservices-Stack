package com.miniblog.query.mapper;

import com.miniblog.post.avro.PostCreatedEvent;
import com.miniblog.query.model.Post;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

@Component
public class PostMapper {
    public Post toCreatePost(PostCreatedEvent postCreatedEvent) {
        return Post.builder()
                .postUuid(postCreatedEvent.getPostUuid().toString())
                .userUuid(postCreatedEvent.getUserUuid().toString())
                .nickname(postCreatedEvent.getNickname().toString())
                .title(postCreatedEvent.getTitle().toString())
                .content(postCreatedEvent.getContent().toString())
                .createdDate(Instant.ofEpochMilli(postCreatedEvent.getCreatedDate()))
                .updatedDate(Instant.ofEpochMilli(postCreatedEvent.getUpdatedDate()))
                .likeCount(0)
                .totalViews(0)
                .commentCount(0)
                .build();
    }
}
