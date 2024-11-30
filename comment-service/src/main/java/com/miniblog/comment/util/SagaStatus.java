package com.miniblog.comment.util;

public enum SagaStatus {
    CREATED,
    PROCESSING,
    RETRYING,
    COMPLETED,
    FAILED
}