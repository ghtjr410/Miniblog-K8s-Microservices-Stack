package com.miniblog.comment.dto.response;

public record CommentResponseDTO(
        String commentUuid,
        String postUuid,
        String nickname,
        String content,
        String createdDate,
        String updatedDate
) {
}
