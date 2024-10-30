package com.miniblog.post.dto;

public record PostRequestDTO(
        String nickname,
        String title,
        String content) {
}
