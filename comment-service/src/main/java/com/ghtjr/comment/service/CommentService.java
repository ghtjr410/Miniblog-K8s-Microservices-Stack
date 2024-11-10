package com.ghtjr.comment.service;

import com.ghtjr.comment.dto.CommentRequestDTO;
import com.ghtjr.comment.dto.CommentResponseDTO;
import com.ghtjr.comment.mapper.CommentMapper;
import com.ghtjr.comment.model.Comment;
import com.ghtjr.comment.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Transactional
    public ResponseEntity<CommentResponseDTO> createComment(
            String userUuid,
            CommentRequestDTO commentRequestDTO) {
        try {
            Comment comment = commentMapper.toEntity(userUuid, commentRequestDTO);
            comment = commentRepository.save(comment);

            CommentResponseDTO commentResponseDTO = commentMapper.toResponseDTO(comment);
            log.info("Success Create Comment : id = {}, commentUuid = {] ", comment.getId(), comment.getCommentUuid());

            return ResponseEntity.status(HttpStatus.CREATED).body(commentResponseDTO);
        } catch (Exception ex) {
            log.error("Error Create Comment : ", ex);
            throw new RuntimeException("Error occurred while creating comment", ex);
        }
    }
}
