package com.miniblog.comment.service.comment;

import com.miniblog.comment.dto.request.CommentCreatedRequestDTO;
import com.miniblog.comment.dto.response.CommentResponseDTO;
import com.miniblog.comment.dto.request.CommentUpdatedRequestDTO;
import com.miniblog.comment.exception.NotFoundException;
import com.miniblog.comment.mapper.CommentMapper;
import com.miniblog.comment.model.Comment;
import com.miniblog.comment.repository.comment.CommentRepository;
import com.miniblog.comment.service.outbox.OutboxEventService;
import com.miniblog.comment.util.ProducedEventType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final OutboxEventService outboxEventService;

    @Transactional
    public CommentResponseDTO createComment(
            String userUuid,
            CommentCreatedRequestDTO commentCreatedRequestDTO) {
        UUID userUuidAsUUID = UUID.fromString(userUuid);

        Comment comment = commentMapper.createToEntity(userUuidAsUUID, commentCreatedRequestDTO);
        comment = commentRepository.save(comment);
        outboxEventService.createOutboxEvent(comment, ProducedEventType.COMMENT_CREATED);

        return commentMapper.toResponseDTO(comment);
    }

    @Transactional
    public CommentResponseDTO updateComment(
            String userUuid,
            String commentUuid,
            CommentUpdatedRequestDTO commentUpdatedRequestDTO) {
        UUID userUuidAsUUID = UUID.fromString(userUuid);
        UUID commentUuidAsUUID = UUID.fromString(commentUuid);

        Comment comment = commentRepository.findByCommentUuidAndUserUuid(commentUuidAsUUID, userUuidAsUUID)
                .orElseThrow(() -> new NotFoundException("Comment not found or user not authorized: commentUuid = " + commentUuidAsUUID + ", userUuid = " + userUuidAsUUID));
        commentMapper.updateToEntity(comment, commentUpdatedRequestDTO);
        commentRepository.save(comment);
        outboxEventService.createOutboxEvent(comment, ProducedEventType.COMMENT_UPDATED);

        return commentMapper.toResponseDTO(comment);
    }

    @Transactional
    public void deleteComment(
            String userUuid,
            String postUuid) {
        UUID userUuidAsUUID = UUID.fromString(userUuid);
        UUID commentUuidAsUUID = UUID.fromString(postUuid);

        Comment comment = commentRepository.findByCommentUuidAndUserUuid(commentUuidAsUUID, userUuidAsUUID)
                .orElseThrow(() -> new NotFoundException("Comment not found or user not authorized: commentUuid = " + commentUuidAsUUID + ", userUuid = " + userUuidAsUUID));
        commentRepository.delete(comment);

        outboxEventService.createOutboxEvent(comment, ProducedEventType.COMMENT_DELETED);
    }
}
