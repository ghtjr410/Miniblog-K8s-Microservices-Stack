package com.miniblog.profile.dto.response;

public record ProfileResult(
        ProfileResponseDTO profileResponseDTO,
        boolean created
) {
}
