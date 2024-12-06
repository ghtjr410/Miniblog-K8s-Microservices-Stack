package com.miniblog.post.repository.outbox;

import com.miniblog.post.util.SagaStatus;

import java.util.UUID;

public interface OutboxEventRepositoryCustom {
    /**
     * 특정 eventUuid의 sagaStatus를 원자적으로 업데이트합니다.
     * 현재 상태가 expectedCurrentStatus 중 하나일 때만 업데이트가 발생합니다.
     *
     * @param eventUuid              이벤트 UUID
     * @param expectedCurrentStatus  예상 현재 상태 배열
     * @param newStatus              새로 설정할 상태
     * @param processed              처리 결과
     * @return 업데이트된 행 수 (성공 시 1이어야 함)
     */
    boolean updateSagaStatus(UUID eventUuid, SagaStatus[] expectedCurrentStatus, SagaStatus newStatus, Boolean processed);
}