package com.miniblog.profile.service;

import com.miniblog.profile.dto.ProfileRequestDTO;
import com.miniblog.profile.dto.ProfileResponseDTO;
import com.miniblog.profile.mapper.OutboxMapper;
import com.miniblog.profile.mapper.ProfileMapper;
import com.miniblog.profile.model.OutboxEvent;
import com.miniblog.profile.model.Profile;
import com.miniblog.profile.repository.OutboxEventRepository;
import com.miniblog.profile.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final ProfileMapper profileMapper;
    private final OutboxMapper outboxMapper;

    @Transactional
    public ResponseEntity<ProfileResponseDTO> createOrUpdateProfile(
            String userUuid,
            ProfileRequestDTO profileRequestDTO) {
        try {
            Profile profile = profileRepository.findByUserUuid(userUuid).orElse(null);
            OutboxEvent outboxEvent;
            if (profile != null) {
                // 프로필이 존재하면 업데이트
                profileMapper.updateProfile(profile, profileRequestDTO);
                profile = profileRepository.save(profile);
                outboxEvent = outboxMapper.toUpdatedProfileEntity(profile);
                outboxEvent.setTraceId("abcdefg"); // 나중에 tempo 관련 로직 추가할때 변경하면됨
                outboxEventRepository.save(outboxEvent);
                log.info("Updating profile for userUuid={}", userUuid);
            } else {
                // 프로필이 없으면 생성
                profile = profileMapper.createProfile(userUuid,profileRequestDTO);
                profile = profileRepository.save(profile);
                outboxEvent = outboxMapper.toCreatedProfileEntity(profile);
                outboxEvent.setTraceId("abcdefg"); // 나중에 tempo 관련 로직 추가할때 변경하면됨
                outboxEventRepository.save(outboxEvent);
                log.info("Creating new profile for userUuid={}", userUuid);
            }
            ProfileResponseDTO profileResponseDTO = profileMapper.toResponseDTO(profile);

            return ResponseEntity.status(HttpStatus.OK).body(profileResponseDTO);
        } catch (Exception ex) {
            log.error("Error Create or Update Profile : ", ex);
            // 예외를 서비스계층에서 벗어나게 하여 트랜잭션이 롤백되도록함
            throw new RuntimeException("Error occurred while creating or updating profile", ex);
        }
    }
}
