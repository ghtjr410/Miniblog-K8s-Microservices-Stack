package com.miniblog.post.dto;

import jakarta.validation.constraints.NotBlank;

public record PostUpdateRequestDTO(
        @NotBlank(message = "Title is required.")
        String Title,

        @NotBlank(message = "Content is required")
        String content
) {
}
