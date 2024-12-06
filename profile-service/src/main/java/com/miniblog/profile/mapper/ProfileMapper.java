package com.miniblog.profile.mapper;

import com.miniblog.profile.dto.request.ProfileRequestDTO;
import com.miniblog.profile.dto.response.ProfileResponseDTO;
import com.miniblog.profile.dto.response.ProfileResult;
import com.miniblog.profile.model.Profile;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProfileMapper {
    public Profile createToEntity(UUID userUuid, ProfileRequestDTO profileRequestDTO) {
        return Profile.builder()
                .userUuid(userUuid)
                .nickname(profileRequestDTO.nickname())
                .email(profileRequestDTO.email())
                .title(profileRequestDTO.title())
                .intro(profileRequestDTO.intro())
                .build();
    }

    public void updateToEntity(Profile profile, ProfileRequestDTO profileRequestDTO) {
        profile.setTitle(profileRequestDTO.title());
        profile.setIntro(profileRequestDTO.intro());
    }

    public ProfileResult toResponseDTO(Profile profile, boolean created) {
        ProfileResponseDTO profileResponseDTO = new ProfileResponseDTO(
                profile.getNickname(),
                profile.getEmail(),
                profile.getTitle(),
                profile.getIntro());
        return new ProfileResult(profileResponseDTO, created);
    }
}