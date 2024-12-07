package com.miniblog.post.exception;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    VALIDATION_ERROR("입력값이 유효하지 않습니다."),
    REQUEST_BODY_VALIDATION_ERROR("요청 데이터가 유효하지 않습니다."),
    UNEXPECTED_ERROR("예상치 못한 오류가 발생했습니다."),
    DATABASE_ACCESS_ERROR("데이터베이스 접근 중 오류가 발생했습니다."),
    NOT_FOUND_ERROR("요청한 리소스를 찾을 수 없습니다."),
    UNAUTHORIZED_ERROR("이 작업을 수행할 권한이 없습니다.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
