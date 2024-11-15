package com.miniblog.comment.service;

import com.miniblog.comment.dto.CommentCreatedRequestDTO;
import com.miniblog.comment.dto.CommentDeletedRequestDTO;
import com.miniblog.comment.dto.CommentResponseDTO;
import com.miniblog.comment.dto.CommentUpdatedRequestDTO;
import com.miniblog.comment.exception.CommentNotFoundException;
import com.miniblog.comment.mapper.CommentMapper;
import com.miniblog.comment.model.Comment;
import com.miniblog.comment.repository.CommentRepository;
import com.miniblog.comment.util.EventType;
import com.miniblog.comment.util.TracerUtility;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final OutboxEventService outboxEventService;
    private final TracerUtility tracerUtility;

    @Transactional
    public CommentResponseDTO createComment(
            String userUuid,
            CommentCreatedRequestDTO commentCreatedRequestDTO) {
        try {
            Comment comment = commentMapper.createToEntity(userUuid, commentCreatedRequestDTO);
            comment = commentRepository.save(comment);

            // OutboxEvent 생성 및 저장
            String traceId = tracerUtility.getTraceId();
            outboxEventService.createOutboxEvent(comment, EventType.COMMENT_CREATED, traceId);

            log.info("Success Create Comment : id = {}, commentUuid = {] ", comment.getId(), comment.getCommentUuid());
            return commentMapper.toResponseDTO(comment);
        } catch (Exception ex) {
            log.error("Error Create Comment : ", ex);
            throw new RuntimeException("Error occurred while creating comment", ex);
        }
    }

    @Transactional
    public CommentResponseDTO updateComment(
            String userUuid,
            CommentUpdatedRequestDTO commentUpdatedRequestDTO) {
        try {
            String commentUuid = commentUpdatedRequestDTO.commentUuid();

            Comment comment = commentRepository.findByCommentUuidAndUserUuid(commentUuid, userUuid)
                    .orElseThrow(() -> new CommentNotFoundException("Comment not found or user not authorized: commentUuid = " + commentUuid + ", userUuid = " + userUuid));
            commentMapper.updateToEntity(comment, commentUpdatedRequestDTO);
            commentRepository.save(comment);

            // OutboxEvent 생성 및 저장
            String traceId = tracerUtility.getTraceId();
            outboxEventService.createOutboxEvent(comment, EventType.COMMENT_UPDATED, traceId);

            log.info("Success Update Comment : id = {}, commentUuid = {}", comment.getId(), comment.getCommentUuid());
            return commentMapper.toResponseDTO(comment);
        } catch (Exception ex) {
            log.error("Error Update Comment : ", ex);
            throw new RuntimeException("Error occurred while updating comment", ex);
        }
    }

    @Transactional
    public void deleteComment(
            String userUuid,
            CommentDeletedRequestDTO commentDeletedRequestDTO) {
        try {
            String commentUuid = commentDeletedRequestDTO.commentUuid();

            Comment comment = commentRepository.findByCommentUuidAndUserUuid(commentUuid, userUuid)
                    .orElseThrow(() -> new CommentNotFoundException("Comment not found or user not authorized: commentUuid = " + commentUuid + ", userUuid = " + userUuid));
            commentRepository.delete(comment);

            // OutboxEvent 생성 및 저장
            String traceId = tracerUtility.getTraceId();
            outboxEventService.createOutboxEvent(comment, EventType.COMMENT_DELETED, traceId);

            log.info("Success Delete Comment : id = {}, commentUuid = {}", comment.getId(), comment.getCommentUuid());
        } catch (Exception ex) {
            log.error("Error Delete Comment : ", ex);
            throw new RuntimeException("Error occurred while updating comment", ex);
        }
    }
}
