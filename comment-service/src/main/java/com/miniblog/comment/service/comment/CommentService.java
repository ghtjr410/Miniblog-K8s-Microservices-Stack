package com.miniblog.comment.service.comment;

import com.miniblog.comment.dto.CommentCreatedRequestDTO;
import com.miniblog.comment.dto.CommentDeletedRequestDTO;
import com.miniblog.comment.dto.CommentResponseDTO;
import com.miniblog.comment.dto.CommentUpdatedRequestDTO;
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
        outboxEventService.createOutboxEvent(comment, ProducedEventType.COMMENT_CREATED);

        return commentMapper.toResponseDTO(comment);
    }

    @Transactional
    public CommentResponseDTO updateComment(
            String userUuid,
            CommentUpdatedRequestDTO commentUpdatedRequestDTO) {

        String commentUuid = commentUpdatedRequestDTO.commentUuid();
        Comment comment = commentRepository.findByCommentUuidAndUserUuid(commentUuid, userUuid)
                .orElseThrow(() -> new NotFoundException("Comment not found or user not authorized: commentUuid = " + commentUuid + ", userUuid = " + userUuid));
        commentMapper.updateToEntity(comment, commentUpdatedRequestDTO);
        commentRepository.save(comment);
        outboxEventService.createOutboxEvent(comment, ProducedEventType.COMMENT_UPDATED);

        return commentMapper.toResponseDTO(comment);
    }

    @Transactional
    public void deleteComment(
            String userUuid,
            CommentDeletedRequestDTO commentDeletedRequestDTO) {

        String commentUuid = commentDeletedRequestDTO.commentUuid();
        Comment comment = commentRepository.findByCommentUuidAndUserUuid(commentUuid, userUuid)
                .orElseThrow(() -> new NotFoundException("Comment not found or user not authorized: commentUuid = " + commentUuid + ", userUuid = " + userUuid));
        commentRepository.delete(comment);

        outboxEventService.createOutboxEvent(comment, ProducedEventType.COMMENT_DELETED);
    }
}
