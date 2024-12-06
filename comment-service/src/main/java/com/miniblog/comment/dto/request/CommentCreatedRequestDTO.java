package com.miniblog.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentCreatedRequestDTO(
        @NotBlank(message = "Post UUID is required.")
        String postUuid,

        @NotBlank(message = "Nickname is required.")
        String nickname,

        @NotBlank(message = "Content is required.")
        @Size(min = 1, max = 500, message = "Content는 1자 이상, 500자 이하여야 합니다.")
        String content
) {
}
