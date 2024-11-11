package com.ghtjr.profile.service;

import com.ghtjr.profile.dto.ProfileRequestDTO;
import com.ghtjr.profile.dto.ProfileResponseDTO;
import com.ghtjr.profile.mapper.ProfileMapper;
import com.ghtjr.profile.model.Profile;
import com.ghtjr.profile.repository.ProfileRepository;
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
    private final ProfileMapper profileMapper;

    @Transactional
    public ResponseEntity<ProfileResponseDTO> createOrUpdateProfile(
            String userUuid,
            ProfileRequestDTO profileRequestDTO) {
        try {
            Profile profile = profileRepository.findByUserUuid(userUuid).orElse(null);

            if (profile != null) {
                // 프로필이 존재하면 업데이트
                profileMapper.updateProfile(profile, profileRequestDTO);
                log.info("Updating profile for userUuid={}", userUuid);
            } else {
                // 프로필이 없으면 생성
                profile = profileMapper.toEntity(userUuid,profileRequestDTO);
                log.info("Creating new profile for userUuid={}", userUuid);
            }
            Profile savedProfile = profileRepository.save(profile);
            ProfileResponseDTO profileResponseDTO = profileMapper.toResponseDTO(savedProfile);

            return ResponseEntity.status(HttpStatus.OK).body(profileResponseDTO);
        } catch (Exception ex) {
            log.error("Error Create or Update Profile : ", ex);
            // 예외를 서비스계층에서 벗어나게 하여 트랜잭션이 롤백되도록함
            throw new RuntimeException("Error occurred while creating or updating profile", ex);
        }
    }
}
