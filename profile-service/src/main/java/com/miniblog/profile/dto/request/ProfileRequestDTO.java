package com.miniblog.profile.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ProfileRequestDTO(
        @NotBlank(message = "Nickname is required.")
        String nickname,
        @NotBlank(message = "Email is required.")
        String email,
        String title,
        String intro) {
}