package com.miniblog.comment.dto;

public record CommentRequestDTO(
        String nickname,
        String content) {
}
