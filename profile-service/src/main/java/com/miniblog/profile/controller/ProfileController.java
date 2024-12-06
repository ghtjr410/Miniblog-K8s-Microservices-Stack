package com.miniblog.profile.controller;

import com.miniblog.profile.dto.request.ProfileRequestDTO;
import com.miniblog.profile.dto.response.ProfileResponseDTO;
import com.miniblog.profile.dto.response.ProfileResult;
import com.miniblog.profile.service.profile.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        ProfileResult result = profileService.createOrUpdateProfile(userUuid, profileRequestDTO);
        if (result.created()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result.profileResponseDTO());
        } else {
            return ResponseEntity.ok(result.profileResponseDTO());
        }
    }
}
