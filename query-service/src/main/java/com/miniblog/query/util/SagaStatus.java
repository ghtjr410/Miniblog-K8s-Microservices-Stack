package com.miniblog.query.util;

/*
 * 상태 전이 정의
 * PROCESSING → COMPLETED
 * PROCESSING → FAILED
 * FAILED → RETRYING
 * RETRYING → COMPLETED
 * RETRYING → FAILED
 */

public enum SagaStatus {
    PROCESSING,
    COMPLETED,
    RETRYING,
    FAILED
}
