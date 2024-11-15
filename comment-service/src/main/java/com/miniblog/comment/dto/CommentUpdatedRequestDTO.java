package com.miniblog.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentUpdatedRequestDTO(
        @NotBlank(message = "Comment UUID is required.")
        String commentUuid,

        @NotBlank(message = "Content is required")
        @Size(min = 1, max = 500, message = "Content는 1자 이상 500자 이하여야 합니다")
        String content
) {
}
