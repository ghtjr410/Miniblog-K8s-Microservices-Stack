package com.miniblog.image.exception;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    REQUEST_BODY_VALIDATION_ERROR("요청 데이터가 유효하지 않습니다."),
    UNEXPECTED_ERROR("예상치 못한 오류가 발생했습니다.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}