package com.miniblog.post.controller;

import com.miniblog.post.dto.PostRequestDTO;
import com.miniblog.post.dto.PostResponseDTO;
import com.miniblog.post.service.PostService;
import lombok.RequiredArgsConstructor;
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
            @RequestBody PostRequestDTO postRequestDTO){
        return postService.createPost(userUuid, postRequestDTO);
    }
}
