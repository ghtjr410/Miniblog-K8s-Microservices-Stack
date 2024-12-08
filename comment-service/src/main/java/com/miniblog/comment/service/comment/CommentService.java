package com.miniblog.comment.service.comment;

import com.miniblog.comment.dto.request.CommentCreatedRequestDTO;
import com.miniblog.comment.dto.response.CommentResponseDTO;
import com.miniblog.comment.dto.request.CommentUpdatedRequestDTO;
import com.miniblog.comment.exception.NotFoundException;
import com.miniblog.comment.exception.UnauthorizedException;
import com.miniblog.comment.mapper.comment.CommentMapper;
import com.miniblog.comment.model.Comment;
import com.miniblog.comment.repository.comment.CommentRepository;
import com.miniblog.comment.service.outbox.OutboxEventService;
import com.miniblog.comment.util.ProducedEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final OutboxEventService outboxEventService;

    @Transactional
    public CommentResponseDTO createComment(String userUuid, CommentCreatedRequestDTO commentCreatedRequestDTO) {
        UUID userUuidAsUUID = UUID.fromString(userUuid);

        Comment comment = commentMapper.createToEntity(userUuidAsUUID, commentCreatedRequestDTO);
        comment = commentRepository.save(comment);
        outboxEventService.createOutboxEvent(comment, ProducedEventType.COMMENT_CREATED);

        return commentMapper.toResponseDTO(comment);
    }

    @Transactional
    public CommentResponseDTO updateComment(String userUuid, String commentUuid, CommentUpdatedRequestDTO commentUpdatedRequestDTO) {
        UUID userUuidAsUUID = UUID.fromString(userUuid);
        UUID commentUuidAsUUID = UUID.fromString(commentUuid);
        // 댓글 조회
        Comment comment = commentRepository.findById(commentUuidAsUUID)
                .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다: commentUuid = " + commentUuid));
        // 권한 확인
        if (!comment.getUserUuid().equals(userUuidAsUUID)) {
            throw new UnauthorizedException("이 댓글을 수정할 권한이 없습니다: userUuid = " + userUuid);
        }
        commentMapper.updateToEntity(comment, commentUpdatedRequestDTO);
        commentRepository.save(comment);
        outboxEventService.createOutboxEvent(comment, ProducedEventType.COMMENT_UPDATED);

        return commentMapper.toResponseDTO(comment);
    }

    @Transactional
    public void deleteComment(String userUuid, String commentUuid) {
        UUID userUuidAsUUID = UUID.fromString(userUuid);
        UUID commentUuidAsUUID = UUID.fromString(commentUuid);
        // 댓글 조회
        Comment comment = commentRepository.findById(commentUuidAsUUID)
                .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다: commentUuid = " + commentUuid));
        // 권한 확인
        if (!comment.getUserUuid().equals(userUuidAsUUID)) {
            throw new UnauthorizedException("이 댓글을 삭제할 권한이 없습니다: userUuid = " + userUuid);
        }
        commentRepository.delete(comment);

        outboxEventService.createOutboxEvent(comment, ProducedEventType.COMMENT_DELETED);
    }
}