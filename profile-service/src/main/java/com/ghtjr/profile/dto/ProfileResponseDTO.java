package com.ghtjr.profile.dto;

public record ProfileResponseDTO(
        String nickname,
        String email,
        String title,
        String intro) {
}
