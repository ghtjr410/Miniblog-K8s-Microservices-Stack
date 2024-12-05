package com.miniblog.like.dto;

public record ToggleLikeResult(
        LikeResponseDTO likeResponseDTO,
        boolean created
) {
}
