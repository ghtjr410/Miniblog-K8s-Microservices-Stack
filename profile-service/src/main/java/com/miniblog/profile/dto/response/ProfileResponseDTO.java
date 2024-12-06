package com.miniblog.profile.dto.response;

public record ProfileResponseDTO(
        String nickname,
        String email,
        String title,
        String intro) {
}
