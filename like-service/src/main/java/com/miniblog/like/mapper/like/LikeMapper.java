package com.miniblog.like.mapper.like;

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
                // todo: 이 정보를 제공해야할 이유가 없어보이긴한데. 추후에 noContent로 주는것도 생각해놓자
                like.getLikeUuid().toString(),
                like.getPostUuid().toString());
    }
}
