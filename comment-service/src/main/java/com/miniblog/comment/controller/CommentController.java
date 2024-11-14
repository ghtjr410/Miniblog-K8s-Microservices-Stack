package com.miniblog.comment.controller;

import com.miniblog.comment.dto.CommentRequestDTO;
import com.miniblog.comment.dto.CommentResponseDTO;
import com.miniblog.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDTO> createComment(
            @RequestHeader(value = "X-User-Sub", required = false) String userUuid,
            @RequestBody CommentRequestDTO commentRequestDTO) {
        return commentService.createComment(userUuid, commentRequestDTO);
    }
}
