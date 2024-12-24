package com.miniblog.account.exception;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    VALIDATION_ERROR("입력값이 유효하지 않습니다."),
    UNEXPECTED_ERROR("예상치 못한 오류가 발생했습니다."),
    DATABASE_ACCESS_ERROR("데이터베이스 접근 중 오류가 발생했습니다.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
