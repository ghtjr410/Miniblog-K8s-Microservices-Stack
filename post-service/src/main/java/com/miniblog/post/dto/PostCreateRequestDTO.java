package com.miniblog.post.dto;

import jakarta.validation.constraints.NotBlank;

public record PostCreateRequestDTO(
        @NotBlank(message = "Nickname is required.")
        String nickname,

        @NotBlank(message = "Title is required.")
        String title,

        @NotBlank(message = "Content is required")
        String content) {
}
