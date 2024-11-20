package com.miniblog.viewcount.service;

import com.miniblog.viewcount.exception.NotFoundException;
import com.miniblog.viewcount.mapper.ViewcountMapper;
import com.miniblog.viewcount.model.Viewcount;
import com.miniblog.viewcount.repository.ViewcountRepository;
import com.miniblog.viewcount.util.EventType;
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
    private final ViewcountMapper viewcountMapper;
    private final OutboxEventService outboxEventService;

    @Transactional
    public void incrementViewcount(String postUuid) {
        UUID postUuidAsUUID = UUID.fromString(postUuid);
        int updatedRows = viewcountRepository.incrementViewcount(postUuidAsUUID);
        if (updatedRows == 0) {
            throw new NotFoundException("Post UUID not found: " + postUuid);
        }
        Long totalViews = viewcountRepository.findTotalViewsByPostUuid(postUuidAsUUID);

        Viewcount updatedViewcount = viewcountMapper.updateToEntity(postUuidAsUUID, totalViews);

        log.info("Update Viewcount: {}", updatedViewcount);
        outboxEventService.createOutboxEvent(updatedViewcount, EventType.VIEWCOUNT_UPDATE);
    }

//    @Transactional
//    public void createViewcount(String postUuid) {
//        Viewcount viewcount = new Viewcount();
//        viewcount.setPostUuid(UUID.fromString(postUuid));
//        viewcountRepository.save(viewcount);
//    }
}
