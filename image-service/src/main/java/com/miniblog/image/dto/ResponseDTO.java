package com.miniblog.image.dto;

public record ResponseDTO(
        String presignedUrl,
        String objectKey
) {
}
