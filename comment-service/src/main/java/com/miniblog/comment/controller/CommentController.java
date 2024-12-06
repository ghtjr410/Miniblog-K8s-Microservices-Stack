package com.miniblog.comment.controller;

import com.miniblog.comment.dto.request.CommentCreatedRequestDTO;
import com.miniblog.comment.dto.response.CommentResponseDTO;
import com.miniblog.comment.dto.request.CommentUpdatedRequestDTO;
import com.miniblog.comment.service.comment.CommentService;
import com.miniblog.comment.validation.ValidUuid;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment-service/comments")
@RequiredArgsConstructor
@Validated
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDTO> createComment(
            @RequestHeader(value = "X-User-Sub", required = false) @ValidUuid String userUuid,
            @Valid @RequestBody CommentCreatedRequestDTO commentCreatedRequestDTO) {
        CommentResponseDTO responseDTO = commentService.createComment(userUuid, commentCreatedRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{commentUuid}")
    public ResponseEntity<CommentResponseDTO> updateComment(
            @RequestHeader(value = "X-User-Sub", required = false) @ValidUuid String userUuid,
            @PathVariable @ValidUuid String commentUuid,
            @Valid @RequestBody CommentUpdatedRequestDTO commentUpdatedRequestDTO) {
        CommentResponseDTO responseDTO = commentService.updateComment(userUuid, commentUuid, commentUpdatedRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{commentUuid}")
    public ResponseEntity<?> deleteComment(
            @RequestHeader(value = "X-User-Sub", required = false) @ValidUuid String userUuid,
            @PathVariable @ValidUuid String commentUuid) {
        commentService.deleteComment(userUuid, commentUuid);
        return ResponseEntity.noContent().build();
    }
}
