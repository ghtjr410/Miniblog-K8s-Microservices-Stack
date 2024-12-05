package com.miniblog.like.controller;

import com.miniblog.like.dto.LikeRequestDTO;
import com.miniblog.like.dto.LikeResponseDTO;
import com.miniblog.like.dto.ToggleLikeResult;
import com.miniblog.like.service.like.LikeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/likes")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/toggle")
    public ResponseEntity<LikeResponseDTO> toggleLike(
            @RequestHeader(value = "X-User-Sub", required = false) String userUuid,
            @Valid @RequestBody LikeRequestDTO likeRequestDTO) {
        ToggleLikeResult result = likeService.toggleLike(userUuid, likeRequestDTO);
        if (result.created()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result.likeResponseDTO());
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
