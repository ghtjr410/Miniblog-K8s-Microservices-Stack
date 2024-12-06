package com.miniblog.like.controller;

import com.miniblog.like.dto.LikeResponseDTO;
import com.miniblog.like.dto.ToggleLikeResult;
import com.miniblog.like.service.like.LikeService;
import com.miniblog.like.validation.ValidUuid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/like-service")
@RequiredArgsConstructor
@Validated
public class LikeController {
    private final LikeService likeService;

    // todo: 현재 편의상 한 번의 요청으로 상태를 전환하는 기능을 선택
    // todo: RESTful 관점에서는 향후 두 동작을 명확히 분리하는 것을 고려해야한다.
    @PostMapping("/posts/{postUuid}/likes/toggle")
    public ResponseEntity<LikeResponseDTO> toggleLike(
            @RequestHeader(value = "X-User-Sub", required = false) @ValidUuid String userUuid,
            @PathVariable @ValidUuid String postUuid) {
        ToggleLikeResult result = likeService.toggleLike(userUuid, postUuid);
        if (result.created()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result.likeResponseDTO());
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
