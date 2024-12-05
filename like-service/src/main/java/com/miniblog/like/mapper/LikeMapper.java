package com.miniblog.like.mapper;

import com.miniblog.like.dto.LikeRequestDTO;
import com.miniblog.like.dto.LikeResponseDTO;
import com.miniblog.like.model.Like;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class LikeMapper {
    public Like createToEntity(UUID userUuid, UUID postUuid) {
        return Like.builder()
                .postUuid(postUuid)
                .userUuid(userUuid)
                .build();
    }

    public LikeResponseDTO toResponseDTO(Like like) {
        return new LikeResponseDTO(
                like.getLikeUuid(),
                like.getPostUuid());
    }
}
