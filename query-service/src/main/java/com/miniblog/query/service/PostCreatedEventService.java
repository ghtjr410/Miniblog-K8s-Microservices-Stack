package com.miniblog.query.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniblog.post.avro.PostCreatedEvent;
import com.miniblog.query.mapper.PostMapper;
import com.miniblog.query.model.Post;
import com.miniblog.query.model.ProcessedEvent;
import com.miniblog.query.repository.PostRepository;
import com.miniblog.query.repository.ProcessedEventRepository;
import com.miniblog.query.util.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostCreatedEventService {
    private final PostRepository postRepository;
    private final ProcessedEventRepository processedEventRepository;
    private final ObjectMapper objectMapper;
    private final PostMapper postMapper;

    public void processEvent(String eventUuid, PostCreatedEvent postCreatedEvent) {
        log.info("processEvent called for eventUuid={}", eventUuid);

        // 멱등성 체크 및 상태 업데이트 처리
        ProcessedEvent processedEvent = handleIdempotencyAndUpdateStatus(eventUuid);

        // 이미 처리된 이벤트인 경우 처리 종료
        if (processedEvent == null) {
            return;
        }

        // 상태 검증
        validateSagaStatus(processedEvent, eventUuid);

        try {
            // Post 저장
            savePost(postCreatedEvent);

            // 상태를 COMPLETED로 업데이트
            updateSagaStatus(eventUuid, new SagaStatus[]{SagaStatus.PROCESSING, SagaStatus.RETRYING}, SagaStatus.COMPLETED);
        } catch (Exception ex) {
            log.error("Post 저장 중 오류 발생: eventUuid={}", eventUuid, ex);
            // 상태를 FAILED로 업데이트
            updateSagaStatus(eventUuid, new SagaStatus[]{SagaStatus.PROCESSING, SagaStatus.RETRYING}, SagaStatus.FAILED);
            // 예외를 다시 던져 Kafka의 재시도 로직을 트리거
            throw ex;
        }
    }

    private ProcessedEvent handleIdempotencyAndUpdateStatus(String eventUuid) {
        Optional<ProcessedEvent> optionalProcessedEvent = processedEventRepository.findByEventUuid(eventUuid);
        if (optionalProcessedEvent.isPresent()) {
            ProcessedEvent processedEvent = optionalProcessedEvent.get();
            if (processedEvent.getSagaStatus() != SagaStatus.FAILED) {
                log.info("이미 처리된 이벤트입니다. eventUuid={}", eventUuid);
                return null; // 처리 종료를 알리기 위해 null 반환
            }
            log.info("이전에 실패한 이벤트입니다. 재처리를 시도합니다. eventUuid={}", eventUuid);
            updateSagaStatus(eventUuid, new SagaStatus[]{SagaStatus.FAILED}, SagaStatus.RETRYING);
            return processedEvent;
        } else {
            ProcessedEvent processedEvent = new ProcessedEvent();
            processedEvent.setEventUuid(eventUuid);
            processedEvent.setSagaStatus(SagaStatus.PROCESSING);
            processedEventRepository.save(processedEvent);
            return processedEvent;
        }
    }

    private void validateSagaStatus(ProcessedEvent processedEvent, String eventUuid) {
        if (processedEvent == null) {
            log.warn("ProcessedEvent가 null입니다. eventUuid={}", eventUuid);
            throw new IllegalStateException("ProcessedEvent가 null입니다.");
        }

        if (processedEvent.getSagaStatus() != SagaStatus.PROCESSING &&
                processedEvent.getSagaStatus() != SagaStatus.RETRYING) {
            log.warn("현재 sagaStatus가 PROCESSING이나 RETRYING 상태가 아닙니다. 처리 중단. eventUuid={}, currentStatus={}",
                    eventUuid, processedEvent.getSagaStatus());
            throw new IllegalStateException("현재 sagaStatus가 PROCESSING이나 RETRYING 상태가 아닙니다.");
        }
    }

    private void savePost(PostCreatedEvent postCreatedEvent) {
        Post post = postMapper.toCreatePost(postCreatedEvent);
        postRepository.save(post);
        log.info("Post 저장 완료: PostUuid={}", post.getPostUuid());
    }

    private void updateSagaStatus(String eventUuid, SagaStatus[] expectedStatuses, SagaStatus newStatus) {
        boolean updated = processedEventRepository.updateSagaStatus(eventUuid, expectedStatuses, newStatus);
        if (!updated) {
            log.warn("sagaStatus를 {}로 업데이트하지 못했습니다. eventUuid={}", newStatus, eventUuid);
            throw new IllegalStateException("sagaStatus를 " + newStatus + "로 업데이트하지 못했습니다.");
        }
    }
}