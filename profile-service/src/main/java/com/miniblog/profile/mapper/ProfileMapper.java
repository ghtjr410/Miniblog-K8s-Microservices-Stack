package com.miniblog.profile.mapper;

import com.miniblog.profile.dto.ProfileRequestDTO;
import com.miniblog.profile.dto.ProfileResponseDTO;
import com.miniblog.profile.model.Profile;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProfileMapper {
    public Profile createProfile(String userUuid, ProfileRequestDTO profileRequestDTO) {
        return Profile.builder()
                .profileUuid(UUID.randomUUID().toString())
                .userUuid(userUuid)
                .nickname(profileRequestDTO.nickname())
                .email(profileRequestDTO.email())
                .title(profileRequestDTO.title())
                .intro(profileRequestDTO.intro())
                .build();
    }

    public void updateProfile(Profile profile, ProfileRequestDTO profileRequestDTO) {
        if (profileRequestDTO.nickname() != null) {
            profile.setNickname(profileRequestDTO.nickname());
        }
        if (profileRequestDTO.email() != null) {
            profile.setEmail(profileRequestDTO.email());
        }
        profile.setTitle(profileRequestDTO.title());
        profile.setIntro(profileRequestDTO.intro());
    }

    public ProfileResponseDTO toResponseDTO(Profile profile) {
        return new ProfileResponseDTO(
                profile.getNickname(),
                profile.getEmail(),
                profile.getTitle(),
                profile.getIntro());
    }
}