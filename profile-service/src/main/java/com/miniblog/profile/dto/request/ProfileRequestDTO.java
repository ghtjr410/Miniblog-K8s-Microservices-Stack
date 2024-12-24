package com.miniblog.profile.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProfileRequestDTO(
        @NotBlank(message = "Nickname is required.")
        String nickname,
        @NotBlank(message = "Email is required.")
        String email,
        @Size(max = 10, message = "Title can be up to 10 characters long.")
        String title,
        @Size(max = 50, message = "Intro can be up to 50 characters long.")
        String intro) {
}