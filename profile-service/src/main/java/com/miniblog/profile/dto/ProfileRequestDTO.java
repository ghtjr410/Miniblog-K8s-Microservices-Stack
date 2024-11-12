package com.miniblog.profile.dto;

public record ProfileRequestDTO(
        String nickname,
        String email,
        String title,
        String intro) {
}