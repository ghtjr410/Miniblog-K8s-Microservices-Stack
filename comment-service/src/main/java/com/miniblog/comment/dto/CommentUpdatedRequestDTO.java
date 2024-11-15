package com.miniblog.comment.dto;

public record CommentUpdatedRequestDTO(
        String commentUuid,
        String content) {
}
