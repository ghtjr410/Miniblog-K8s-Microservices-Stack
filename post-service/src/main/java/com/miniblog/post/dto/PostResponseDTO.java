package com.miniblog.post.dto;

public record PostResponseDTO(
        String postUuid,
        String nickname,
        String title,
        String content,
        String createdDate,
        String updatedDate
) {
}
