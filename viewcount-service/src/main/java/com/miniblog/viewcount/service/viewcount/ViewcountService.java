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
                .orElseThrow(() -> new NotFoundException("Post UUID not found: " + postUuid));

        // totalViews 증가
        viewcount.setTotalViews(viewcount.getTotalViews() + 1);

        // 낙관적 락 충돌이 발생할 경우 ObjectOptimisticLockingFailureException 발생
        Viewcount updatedViewcount = viewcountRepository.save(viewcount);

        // Outbox 이벤트 발행
        outboxEventService.createOutboxEvent(updatedViewcount, ProducedEventType.VIEWCOUNT_UPDATE);
    }
}
