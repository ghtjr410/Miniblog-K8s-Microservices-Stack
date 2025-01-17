package com.miniblog.comment.mapper.comment;

import com.miniblog.comment.dto.request.CommentCreatedRequestDTO;
import com.miniblog.comment.dto.response.CommentResponseDTO;
import com.miniblog.comment.dto.request.CommentUpdatedRequestDTO;
import com.miniblog.comment.model.Comment;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CommentMapper {
    public Comment createToEntity(UUID userUuid, CommentCreatedRequestDTO commentRequestDTO) {
        return Comment.builder()
                .postUuid(UUID.fromString(commentRequestDTO.postUuid()))
                .userUuid(userUuid)
                .nickname(commentRequestDTO.nickname())
                .content(commentRequestDTO.content())
                .build();
    }

    public void updateToEntity(Comment comment, CommentUpdatedRequestDTO commentUpdatedRequestDTO) {
        comment.setContent(commentUpdatedRequestDTO.content());
    }

    public CommentResponseDTO toResponseDTO(Comment comment) {
        return new CommentResponseDTO(
                comment.getCommentUuid().toString(),
                comment.getPostUuid().toString(),
                comment.getNickname(),
                comment.getContent(),
                comment.getCreatedDate().toString(),
                comment.getUpdatedDate().toString());
    }
}
