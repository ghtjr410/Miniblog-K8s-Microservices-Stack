package com.miniblog.viewcount.service.viewcount;

import com.miniblog.viewcount.exception.NotFoundException;
import com.miniblog.viewcount.model.Viewcount;
import com.miniblog.viewcount.repository.viewcount.ViewcountRepository;
import com.miniblog.viewcount.service.outbox.OutboxEventService;
import com.miniblog.viewcount.util.ProducedEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ViewcountService {
    private final ViewcountRepository viewcountRepository;
    private final OutboxEventService outboxEventService;

    @Transactional
    public void incrementViewcount(String postUuid) {
        UUID postUuidAsUUID = UUID.fromString(postUuid);

        Viewcount viewcount = viewcountRepository.findById(postUuidAsUUID)
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다: postUuid = " + postUuid));

        // 기존 카운트
        long oldCount = viewcount.getTotalViews();

        // UPDATE 쿼리 (1회)
        viewcountRepository.incrementViewcount(postUuidAsUUID);

        // 재조회 대신, 증가된 값을 직접 계산
        long updatedCount = oldCount + 1;

        // 이벤트 생성 시, updatedCount 사용
        viewcount.setTotalViews(updatedCount);
        // 대부분의 뷰 카운트 로직은 “정확도”보단 “대략적인 누적”을 중요 MySQL은 Returning지원X
        // Outbox 이벤트 발행
        // Virtual Threads 활용한 비동기 이벤트 처리
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            executor.submit(() -> {
                outboxEventService.createOutboxEvent(viewcount, ProducedEventType.VIEWCOUNT_UPDATE);
            });
        }
    }
}
