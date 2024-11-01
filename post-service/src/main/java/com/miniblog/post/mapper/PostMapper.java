package com.miniblog.post.mapper;

import com.miniblog.post.dto.PostRequestDTO;
import com.miniblog.post.dto.PostResponseDTO;
import com.miniblog.post.model.Post;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class PostMapper {
    public Post toEntity(String userUuid, PostRequestDTO postRequestDTO) {
        return Post.builder()
                .postUuid(UUID.randomUUID().toString())
                .userUuid(userUuid)
                .nickname(postRequestDTO.nickname())
                .title(postRequestDTO.title())
                .content(postRequestDTO.content())
                .createdDate(new Date())
                .updatedDate(new Date())
                .build();
    }
    public PostResponseDTO toResponseDTO(Post post) {
        return new PostResponseDTO(
                post.getNickname(),
                post.getTitle(),
                post.getContent());
    }
}