package com.miniblog.post.controller;

import com.miniblog.post.dto.PostCreateRequestDTO;
import com.miniblog.post.dto.PostResponseDTO;
import com.miniblog.post.dto.PostUpdateRequestDTO;
import com.miniblog.post.service.post.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(
            @RequestHeader(value = "X-User-Sub", required = false) String userUuid,
            @Valid @RequestBody PostCreateRequestDTO postRequestDTO) {
        PostResponseDTO responseDTO = postService.createPost(userUuid, postRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{postUuid}")
    public ResponseEntity<PostResponseDTO> updatePost(
            @RequestHeader(value = "X-User-Sub", required = false) String userUuid,
            @PathVariable
            @NotBlank
            @Size(min = 36, max = 36)
            @Pattern(regexp = "^[0-9a-f\\-]{36}$") String postUuid,
            @Valid @RequestBody PostUpdateRequestDTO postUpdateRequestDTO) {
        PostResponseDTO responseDTO = postService.updatePost(userUuid, postUuid, postUpdateRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{postUuid}")
    public ResponseEntity<?> deletePost(
            @RequestHeader(value = "X-User-Sub", required = false) String userUuid,
            @PathVariable
            @NotBlank
            @Size(min = 36, max = 36)
            @Pattern(regexp = "^[0-9a-f\\-]{36}$") String postUuid) {
            postService.deletePost(userUuid, postUuid);
            return ResponseEntity.noContent().build();
    }

}
