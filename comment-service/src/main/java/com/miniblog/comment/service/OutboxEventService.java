package com.miniblog.comment.service;

import com.miniblog.comment.mapper.OutboxMapper;
import com.miniblog.comment.model.Comment;
import com.miniblog.comment.model.OutboxEvent;
import com.miniblog.comment.repository.OutboxEventRepository;
import com.miniblog.comment.util.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OutboxEventService {
    private final OutboxEventRepository outboxEventRepository;
    private final OutboxMapper outboxMapper;

    public void createOutboxEvent(Comment comment, EventType eventType, String traceId) {
        try {
            OutboxEvent outboxEvent = outboxMapper.toOutboxEvent(comment, eventType);
            outboxEvent.setTraceId(traceId);
            outboxEventRepository.save(outboxEvent);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to save OutboxEvent", ex); // 예외를 던짐
        }
    }
}
