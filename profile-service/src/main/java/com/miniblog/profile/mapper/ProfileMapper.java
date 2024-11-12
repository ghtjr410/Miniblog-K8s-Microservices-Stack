package com.miniblog.profile.mapper;

import com.miniblog.profile.dto.ProfileRequestDTO;
import com.miniblog.profile.dto.ProfileResponseDTO;
import com.miniblog.profile.model.Profile;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProfileMapper {
    public void toEntity(Profile profile, ProfileRequestDTO profileRequestDTO) {
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