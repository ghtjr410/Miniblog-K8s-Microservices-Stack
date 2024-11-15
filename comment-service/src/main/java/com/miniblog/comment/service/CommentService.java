package com.miniblog.comment.service;

import com.miniblog.comment.dto.CommentCreatedRequestDTO;
import com.miniblog.comment.dto.CommentDeletedRequestDTO;
import com.miniblog.comment.dto.CommentResponseDTO;
import com.miniblog.comment.dto.CommentUpdatedRequestDTO;
import com.miniblog.comment.mapper.CommentMapper;
import com.miniblog.comment.mapper.OutboxMapper;
import com.miniblog.comment.model.Comment;
import com.miniblog.comment.model.OutboxEvent;
import com.miniblog.comment.repository.CommentRepository;
import com.miniblog.comment.repository.OutboxEventRepository;
import com.miniblog.comment.util.EventType;
import io.micrometer.tracing.Tracer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final OutboxEventRepository outboxEventRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final OutboxMapper outboxMapper;
    private final Tracer tracer;

    @Transactional
    public ResponseEntity<CommentResponseDTO> createComment(
            String userUuid,
            CommentCreatedRequestDTO commentCreatedRequestDTO) {
        try {
            Comment comment = commentMapper.createToEntity(userUuid, commentCreatedRequestDTO);
            comment = commentRepository.save(comment);
            
            // OutboxEvent 생성 및 저장
            OutboxEvent outboxEvent = outboxMapper.toOutboxEvent(comment, EventType.COMMENT_CREATED);
            String traceId = tracer.currentSpan().context().traceId();
            outboxEvent.setTraceId(traceId);
            outboxEventRepository.save(outboxEvent);

            log.info("Success Create Comment : id = {}, commentUuid = {] ", comment.getId(), comment.getCommentUuid());
            CommentResponseDTO commentResponseDTO = commentMapper.toResponseDTO(comment);

            return ResponseEntity.status(HttpStatus.CREATED).body(commentResponseDTO);
        } catch (Exception ex) {
            log.error("Error Create Comment : ", ex);
            throw new RuntimeException("Error occurred while creating comment", ex);
        }
    }

    @Transactional
    public ResponseEntity<CommentResponseDTO> updateComment(
            String userUuid,
            CommentUpdatedRequestDTO commentUpdatedRequestDTO) {
        try {
            String commentUuid = commentUpdatedRequestDTO.commentUuid();

            Optional<Comment> optionalComment = commentRepository.findByCommentUuidAndUserUuid(commentUuid, userUuid);
            if(optionalComment.isPresent()) {
                Comment comment = optionalComment.get();
                commentMapper.updateToEntity(comment, commentUpdatedRequestDTO);
                commentRepository.save(comment);

                // OutboxEvent 생성 및 저장
                OutboxEvent outboxEvent = outboxMapper.toOutboxEvent(comment, EventType.COMMENT_UPDATED);
                String traceId = tracer.currentSpan().context().traceId();
                outboxEvent.setTraceId(traceId);
                outboxEventRepository.save(outboxEvent);

                log.info("Success Update Comment : id = {}, commentUuid = {}", comment.getId(), comment.getCommentUuid());
                CommentResponseDTO commentResponseDTO = commentMapper.toResponseDTO(comment);

                return ResponseEntity.status(HttpStatus.OK).body(commentResponseDTO);
            } else {
                log.warn("Comment not found or user not authorized: commentUuid = {}, userUuid = {}", commentUuid, userUuid);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception ex) {
            log.error("Error Update Comment : ", ex);
            throw new RuntimeException("Error occurred while updating comment", ex);
        }
    }

    @Transactional
    public ResponseEntity<?> deleteComment(
            String userUuid,
            CommentDeletedRequestDTO commentDeletedRequestDTO) {
        try {
            String commentUuid = commentDeletedRequestDTO.commentUuid();

            Optional<Comment> optionalComment = commentRepository.findByCommentUuidAndUserUuid(commentUuid, userUuid);
            if (optionalComment.isPresent()) {
                Comment comment = optionalComment.get();
                commentRepository.delete(comment);

                // OutboxEvent 생성 및 저장
                OutboxEvent outboxEvent = outboxMapper.toOutboxEvent(comment, EventType.COMMENT_DELETED);
                String traceId = tracer.currentSpan().context().traceId();
                outboxEvent.setTraceId(traceId);
                outboxEventRepository.save(outboxEvent);

                log.info("Success Delete Comment : id = {}, commentUuid = {}", comment.getId(), comment.getCommentUuid());
                return ResponseEntity.status(HttpStatus.OK).body("Success Deleted Comment");
            } else {
                log.warn("Comment not found or user not authorized: commentUuid = {}, userUuid = {}", commentUuid, userUuid);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception ex) {
            log.error("Error Delete Comment : ", ex);
            throw new RuntimeException("Error occurred while updating comment", ex);
        }
    }
}
