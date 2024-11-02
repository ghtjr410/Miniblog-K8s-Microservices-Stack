package com.miniblog.query.repository;

import com.miniblog.query.util.SagaStatus;

public interface ProcessedEventRepositoryCustom {
    /**
     * 특정 eventUuid의 sagaStatus를 원자적으로 업데이트합니다.
     * 현재 상태가 expectedCurrentStatus일때에만 new Status로 변경합니다
     *
     * @param eventUuid   이벤트 UUID
     * @param expectedCurrentStatus 예상 현재 상태
     * @param newStatus 변경할 새로운 상태
     * @return 상태 변경 여부
     */
    boolean updateSagaStatus(String eventUuid, SagaStatus[] expectedCurrentStatus, SagaStatus newStatus);
}
