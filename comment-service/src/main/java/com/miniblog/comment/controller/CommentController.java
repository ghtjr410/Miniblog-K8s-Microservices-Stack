package com.miniblog.comment.controller;

import com.miniblog.comment.dto.CommentCreatedRequestDTO;
import com.miniblog.comment.dto.CommentDeletedRequestDTO;
import com.miniblog.comment.dto.CommentResponseDTO;
import com.miniblog.comment.dto.CommentUpdatedRequestDTO;
import com.miniblog.comment.service.comment.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        CommentResponseDTO responseDTO = commentService.createComment(userUuid, commentCreatedRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping
    public ResponseEntity<CommentResponseDTO> updateComment(
            @RequestHeader(value = "X-User-Sub", required = false) String userUuid,
            @Valid @RequestBody CommentUpdatedRequestDTO commentUpdatedRequestDTO) {
        CommentResponseDTO responseDTO = commentService.updateComment(userUuid, commentUpdatedRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteComment(
            @RequestHeader(value = "X-User-Sub", required = false) String userUuid,
            @Valid @RequestBody CommentDeletedRequestDTO commentDeletedRequestDTO) {
        commentService.deleteComment(userUuid, commentDeletedRequestDTO);
        return ResponseEntity.noContent().build();
    }
}
