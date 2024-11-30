package com.miniblog.comment.mapper;

import com.miniblog.comment.dto.CommentCreatedRequestDTO;
import com.miniblog.comment.dto.CommentResponseDTO;
import com.miniblog.comment.dto.CommentUpdatedRequestDTO;
import com.miniblog.comment.model.Comment;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CommentMapper {
    public Comment createToEntity(String userUuid, CommentCreatedRequestDTO commentRequestDTO) {
        return Comment.builder()
                .postUuid(UUID.fromString(commentRequestDTO.postUuid()))
                .userUuid(UUID.fromString(userUuid))
                .nickname(commentRequestDTO.nickname())
                .content(commentRequestDTO.content())
                .build();
    }

    public void updateToEntity(Comment comment, CommentUpdatedRequestDTO commentUpdatedRequestDTO) {
        comment.setContent(commentUpdatedRequestDTO.content());
    }

    public CommentResponseDTO toResponseDTO(Comment comment) {
        return new CommentResponseDTO(
                comment.getNickname(),
                comment.getContent());
    }
}
