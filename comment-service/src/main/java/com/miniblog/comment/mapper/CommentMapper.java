package com.miniblog.comment.mapper;

import com.miniblog.comment.dto.CommentRequestDTO;
import com.miniblog.comment.dto.CommentResponseDTO;
import com.miniblog.comment.model.Comment;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class CommentMapper {
    public Comment toEntity(String userUuid, CommentRequestDTO commentRequestDTO) {
        return Comment.builder()
                .commentUuid(UUID.randomUUID().toString())
                .userUuid(userUuid)
                .nickname(commentRequestDTO.nickname())
                .content(commentRequestDTO.content())
                .createdDate(new Date())
                .updatedDate(new Date())
                .build();
    }

    public CommentResponseDTO toResponseDTO(Comment comment) {
        return new CommentResponseDTO(
                comment.getNickname(),
                comment.getContent());
    }
}
