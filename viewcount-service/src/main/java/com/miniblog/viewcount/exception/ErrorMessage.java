package com.miniblog.viewcount.exception;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    VALIDATION_ERROR("입력값이 유효하지 않습니다."),
    REQUEST_BODY_VALIDATION_ERROR("요청 데이터가 유효하지 않습니다."),
    UNEXPECTED_ERROR("예상치 못한 오류가 발생했습니다."),
    OPTIMISTIC_LOCK_ERROR("이미 해당 데이터를 수정했습니다. 최신 상태를 확인해주세요.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
