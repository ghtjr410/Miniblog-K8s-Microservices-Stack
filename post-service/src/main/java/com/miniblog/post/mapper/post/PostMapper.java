package com.miniblog.post.mapper.post;

import com.miniblog.post.dto.PostCreateRequestDTO;
import com.miniblog.post.dto.PostResponseDTO;
import com.miniblog.post.dto.PostUpdateRequestDTO;
import com.miniblog.post.model.Post;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PostMapper {
    public Post createToEntity(String userUuid, PostCreateRequestDTO postRequestDTO) {
        return Post.builder()
                .userUuid(UUID.fromString(userUuid))
                .nickname(postRequestDTO.nickname())
                .title(postRequestDTO.title())
                .content(postRequestDTO.content())
                .build();
    }
    public void updateToEntity(Post post, PostUpdateRequestDTO postUpdateRequestDTO) {
        post.setTitle(postUpdateRequestDTO.Title());
        post.setContent(postUpdateRequestDTO.content());
    }

    public PostResponseDTO toResponseDTO(Post post) {
        return new PostResponseDTO(
                post.getNickname(),
                post.getTitle(),
                post.getContent());
    }
}