package com.ghtjr.profile.controller;

import com.ghtjr.profile.dto.ProfileRequestDTO;
import com.ghtjr.profile.dto.ProfileResponseDTO;
import com.ghtjr.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping
    public ResponseEntity<ProfileResponseDTO> createOrUpdateProfile(
            @RequestHeader(value = "X-User-Sub", required = false) String userUuid,
            @RequestBody ProfileRequestDTO profileRequestDTO) {
        return profileService.createOrUpdateProfile(userUuid, profileRequestDTO);
    }
}
