package com.miniblog.profile.dto;

public record ProfileResponseDTO(
        String nickname,
        String email,
        String title,
        String intro) {
}
