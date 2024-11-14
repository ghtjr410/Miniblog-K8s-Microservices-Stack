package com.miniblog.query.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniblog.profile.avro.ProfileCreatedEvent;
import com.miniblog.query.mapper.ProfileMapper;
import com.miniblog.query.model.ProcessedEvent;
import com.miniblog.query.model.Profile;
import com.miniblog.query.repository.ProcessedEventRepository;
import com.miniblog.query.repository.ProfileRepository;
import com.miniblog.query.util.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileCreatedEventService {
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final EventProcessingHelper eventProcessingHelper;

    public void processEvent(String eventUuid, ProfileCreatedEvent profileCreatedEvent) {
        log.info("processEvent called for eventUuid={}", eventUuid);

        // 멱등성 체크 및 상태 업데이트 처리
        ProcessedEvent processedEvent =  eventProcessingHelper.handleIdempotencyAndUpdateStatus(eventUuid);

        // 이미 처리된 이벤트인 경우 처리 종료
        if (processedEvent == null) {
            return;
        }

        // 상태 검증
        eventProcessingHelper.validateSagaStatus(processedEvent, eventUuid);

        try {
            // Profile 저장
            saveProfile(profileCreatedEvent);

            // 상태를 COMPLETED로 업데이트
            eventProcessingHelper.updateSagaStatus(eventUuid, new SagaStatus[]{SagaStatus.PROCESSING, SagaStatus.RETRYING}, SagaStatus.COMPLETED);
        } catch (Exception ex) {
            log.error("Post 저장 중 오류 발생: eventUuid={}", eventUuid, ex);
            // 상태를 FAILED로 업데이트
            eventProcessingHelper.updateSagaStatus(eventUuid, new SagaStatus[]{SagaStatus.PROCESSING, SagaStatus.RETRYING}, SagaStatus.FAILED);
            // 예외를 다시 던져 Kafka의 재시도 로직을 트리거
            throw ex;
        }
    }

    private void saveProfile(ProfileCreatedEvent profileCreatedEvent) {
        Profile profile = profileMapper.toCreatedProfile(profileCreatedEvent);
        profileRepository.save(profile);
        log.info("Profile 저장 완료: PostUuid={}", profile.getProfileUuid());
    }

}
