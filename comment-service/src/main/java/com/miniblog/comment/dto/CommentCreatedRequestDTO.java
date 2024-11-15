package com.miniblog.comment.dto;

public record CommentCreatedRequestDTO(
        String postUuid,
        String nickname,
        String content) {
}
