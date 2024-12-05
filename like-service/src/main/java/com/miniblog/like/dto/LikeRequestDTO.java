package com.miniblog.like.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LikeRequestDTO(
        @NotBlank(message = "Post UUID is required.")
        @Size(min = 1, message = "Post UUID는 1자 이상이어야 합니다.")
        String postUuid
) {
}
