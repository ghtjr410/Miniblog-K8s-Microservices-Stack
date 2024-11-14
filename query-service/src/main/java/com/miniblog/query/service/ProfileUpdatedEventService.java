package com.miniblog.query.service;

import com.miniblog.profile.avro.ProfileUpdatedEvent;
import com.miniblog.query.mapper.ProfileMapper;
import com.miniblog.query.model.ProcessedEvent;
import com.miniblog.query.model.Profile;
import com.miniblog.query.repository.ProfileRepository;
import com.miniblog.query.util.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileUpdatedEventService {
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final EventProcessingHelper eventProcessingHelper;

    public void processEvent(String eventUuid, ProfileUpdatedEvent profileUpdatedEvent) {
        log.info("processEvent called for eventUuid={}", eventUuid);

        // 멱등성 체크 및 상태 업데이트 처리
        ProcessedEvent processedEvent = eventProcessingHelper.handleIdempotencyAndUpdateStatus(eventUuid);

        // 이미 처리된 이벤트인 경우 처리 종료
        if (processedEvent == null) {
            return;
        }

        // 상태 검증
        eventProcessingHelper.validateSagaStatus(processedEvent, eventUuid);

        try {
            // Profile 저장
            saveProfile(profileUpdatedEvent);

            // 상태를 COMPLETED로 업데이트
            eventProcessingHelper.updateSagaStatus(eventUuid, new SagaStatus[]{SagaStatus.PROCESSING, SagaStatus.RETRYING}, SagaStatus.COMPLETED);
        } catch (Exception ex) {
            log.error("Profile 저장 중 오류 발생: eventUuid={}", eventUuid, ex);
            // 상태를 FAILED로 업데이트
            eventProcessingHelper.updateSagaStatus(eventUuid, new SagaStatus[]{SagaStatus.PROCESSING, SagaStatus.RETRYING}, SagaStatus.FAILED);
            // 예외를 다시 던져 Kafka의 재시도 로직을 트리거
            throw ex;
        }
    }

    private void saveProfile(ProfileUpdatedEvent profileUpdatedEvent) {
        Profile profile = profileRepository.findByProfileUuid(profileUpdatedEvent.getProfileUuid().toString())
                .orElseThrow(() -> new IllegalStateException("프로필을 찾을 수 없습니다. ProfileUuid=" + profileUpdatedEvent.getProfileUuid()));
        profileMapper.toUpdatedProfile(profile, profileUpdatedEvent);
        profileRepository.save(profile);
    }
}
