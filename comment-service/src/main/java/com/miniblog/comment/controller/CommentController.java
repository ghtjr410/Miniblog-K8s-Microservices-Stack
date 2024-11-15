package com.miniblog.comment.controller;

import com.miniblog.comment.dto.CommentCreatedRequestDTO;
import com.miniblog.comment.dto.CommentDeletedRequestDTO;
import com.miniblog.comment.dto.CommentResponseDTO;
import com.miniblog.comment.dto.CommentUpdatedRequestDTO;
import com.miniblog.comment.service.CommentService;
import jakarta.validation.Valid;
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
            @Valid @RequestBody CommentCreatedRequestDTO commentCreatedRequestDTO) {
        return commentService.createComment(userUuid, commentCreatedRequestDTO);
    }

    @PutMapping
    public ResponseEntity<CommentResponseDTO> updateComment(
            @RequestHeader(value = "X-User-Sub", required = false) String userUuid,
            @Valid @RequestBody CommentUpdatedRequestDTO commentUpdatedRequestDTO) {
        return commentService.updateComment(userUuid, commentUpdatedRequestDTO);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteComment(
            @RequestHeader(value = "X-User-Sub", required = false) String userUuid,
            @Valid @RequestBody CommentDeletedRequestDTO commentDeletedRequestDTO) {
        return commentService.deleteComment(userUuid, commentDeletedRequestDTO);
    }
}
