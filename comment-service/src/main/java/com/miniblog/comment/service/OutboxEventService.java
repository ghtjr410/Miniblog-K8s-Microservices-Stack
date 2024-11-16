package com.miniblog.comment.service;

import com.miniblog.comment.mapper.OutboxMapper;
import com.miniblog.comment.model.Comment;
import com.miniblog.comment.model.OutboxEvent;
import com.miniblog.comment.repository.OutboxEventRepository;
import com.miniblog.comment.util.EventType;
import com.miniblog.comment.util.TracerUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OutboxEventService {
    private final OutboxEventRepository outboxEventRepository;
    private final OutboxMapper outboxMapper;
    private final TracerUtility tracerUtility;

    public void createOutboxEvent(Comment comment, EventType eventType) {
        String traceId = tracerUtility.getTraceId();
        OutboxEvent outboxEvent = outboxMapper.toOutboxEvent(comment, eventType);
        outboxEvent.setTraceId(traceId);
        outboxEventRepository.save(outboxEvent);
    }
}
