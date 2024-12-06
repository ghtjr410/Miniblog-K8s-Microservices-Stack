package com.miniblog.profile.dto.request;

public record ProfileRequestDTO(
        String nickname,
        String email,
        String title,
        String intro) {
}