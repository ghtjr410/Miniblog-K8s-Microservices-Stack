package com.miniblog.comment.dto;

import jakarta.validation.constraints.NotBlank;

public record CommentDeletedRequestDTO(
        @NotBlank(message = "Comment UUID is required.")
        String commentUuid
) {
}
