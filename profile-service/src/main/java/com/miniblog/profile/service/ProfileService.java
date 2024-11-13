package com.miniblog.profile.service;

import com.miniblog.profile.dto.ProfileRequestDTO;
import com.miniblog.profile.dto.ProfileResponseDTO;
import com.miniblog.profile.mapper.OutboxMapper;
import com.miniblog.profile.mapper.ProfileMapper;
import com.miniblog.profile.model.OutboxEvent;
import com.miniblog.profile.model.Profile;
import com.miniblog.profile.repository.OutboxEventRepository;
import com.miniblog.profile.repository.ProfileRepository;
import io.micrometer.tracing.Tracer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final ProfileMapper profileMapper;
    private final OutboxMapper outboxMapper;
    private final Tracer tracer;

    @Transactional
    public ResponseEntity<ProfileResponseDTO> createOrUpdateProfile(
            String userUuid,
            ProfileRequestDTO profileRequestDTO) {
        try {
            boolean isNew = false;
            Profile profile = profileRepository.findByUserUuid(userUuid).orElse(null);

            if (profile == null) {
                // 프로필이 없으면 새로 생성
                profile = new Profile();
                profile.setProfileUuid(UUID.randomUUID().toString());
                profile.setUserUuid(userUuid);
                isNew = true;
            }

            // 프로필 업데이트 (새 프로필이든 기존 프로필이든 동일하게 처리)
            profileMapper.toEntity(profile, profileRequestDTO);
            profile = profileRepository.save(profile);
            
            // OutboxEvent 생성 및 저장
            OutboxEvent outboxEvent = outboxMapper.toOutboxEvent(profile, isNew);
            String traceId = tracer.currentSpan().context().traceId();
            outboxEvent.setTraceId(traceId); // 나중에 tempo 관련 로직 추가 예정
            outboxEventRepository.save(outboxEvent);

            log.info("{} profile for userUuid={}", isNew ? "Creating new" : "Updating", userUuid);
            ProfileResponseDTO profileResponseDTO = profileMapper.toResponseDTO(profile);

            return ResponseEntity.status(HttpStatus.OK).body(profileResponseDTO);
        } catch (Exception ex) {
            log.error("Error Create or Update Profile : ", ex);
            // 예외를 서비스계층에서 벗어나게 하여 트랜잭션이 롤백되도록함
            throw new RuntimeException("Error occurred while creating or updating profile", ex);
        }
    }
}
