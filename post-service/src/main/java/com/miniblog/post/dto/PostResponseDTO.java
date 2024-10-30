package com.miniblog.post.dto;

public record PostResponseDTO(
        String nickname,
        String title,
        String content) {
}
