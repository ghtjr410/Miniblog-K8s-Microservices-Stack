package com.miniblog.profile.service.profile;

import com.miniblog.profile.dto.request.ProfileRequestDTO;
import com.miniblog.profile.dto.response.ProfileResult;
import com.miniblog.profile.mapper.profile.ProfileMapper;
import com.miniblog.profile.model.Profile;
import com.miniblog.profile.repository.profile.ProfileRepository;
import com.miniblog.profile.service.outbox.OutboxEventService;
import com.miniblog.profile.util.ProducedEventType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final OutboxEventService outboxEventService;

    @Transactional
    public ProfileResult createOrUpdateProfile(
            String userUuid,
            ProfileRequestDTO profileRequestDTO) {
        UUID userUuidAsUUID = UUID.fromString(userUuid);
        Optional<Profile> optionalProfile = profileRepository.findByUserUuid(userUuidAsUUID);

        if (optionalProfile.isEmpty()) {
            // 프로필 생성
            Profile newProfile = profileMapper.createToEntity(userUuidAsUUID, profileRequestDTO);
            profileRepository.save(newProfile);
            outboxEventService.createOutboxEvent(newProfile, ProducedEventType.PROFILE_CREATED);

            return profileMapper.toResponseDTO(newProfile, true);
        } else {
            // 프로필 수정
            Profile exsistingProfile = optionalProfile.get();
            profileMapper.updateToEntity(exsistingProfile, profileRequestDTO);
            profileRepository.save(exsistingProfile);
            outboxEventService.createOutboxEvent(exsistingProfile, ProducedEventType.PROFILE_UPDATED);

            return profileMapper.toResponseDTO(exsistingProfile, false);
        }
    }
}