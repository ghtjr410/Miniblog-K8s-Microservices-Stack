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

    @Transactional
    public CommentResponseDTO createComment(
            String userUuid,
            CommentCreatedRequestDTO commentCreatedRequestDTO) {

        Comment comment = commentMapper.createToEntity(userUuid, commentCreatedRequestDTO);
        comment = commentRepository.save(comment);
        outboxEventService.createOutboxEvent(comment, EventType.COMMENT_CREATED);

        return commentMapper.toResponseDTO(comment);
    }

    @Transactional
    public CommentResponseDTO updateComment(
            String userUuid,
            CommentUpdatedRequestDTO commentUpdatedRequestDTO) {

        String commentUuid = commentUpdatedRequestDTO.commentUuid();
        Comment comment = commentRepository.findByCommentUuidAndUserUuid(commentUuid, userUuid)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found or user not authorized: commentUuid = " + commentUuid + ", userUuid = " + userUuid));
        commentMapper.updateToEntity(comment, commentUpdatedRequestDTO);
        commentRepository.save(comment);
        outboxEventService.createOutboxEvent(comment, EventType.COMMENT_UPDATED);

        return commentMapper.toResponseDTO(comment);
    }

    @Transactional
    public void deleteComment(
            String userUuid,
            CommentDeletedRequestDTO commentDeletedRequestDTO) {

        String commentUuid = commentDeletedRequestDTO.commentUuid();
        Comment comment = commentRepository.findByCommentUuidAndUserUuid(commentUuid, userUuid)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found or user not authorized: commentUuid = " + commentUuid + ", userUuid = " + userUuid));
        commentRepository.delete(comment);

        outboxEventService.createOutboxEvent(comment, EventType.COMMENT_DELETED);
    }
}
