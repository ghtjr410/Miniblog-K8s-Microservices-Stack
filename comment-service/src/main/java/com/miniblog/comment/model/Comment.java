package com.miniblog.comment.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comment")
@Builder
@Slf4j
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String commentUuid;

    @Column(nullable = false)
    private String postUuid;

    @Column(nullable = false)
    private String userUuid;

    @Column(nullable = false)
    private String nickname;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;
    private Instant createdDate;
    private Instant updatedDate;

    @PrePersist // 데이터베이스에 저장되기 직전(INSERT 쿼리 실행 전)에 실행
    public void prePersist() {
        if(commentUuid == null) {
            commentUuid = UUID.randomUUID().toString();
        }
        Instant now = Instant.now();
        this.createdDate = now;
        this.updatedDate = now;
        log.info("Entity is about to be created - CommentUUID: {}, PostUUID: {}, UserUUID: {}, Nickname: {}, Content: {}",
                this.commentUuid, this.postUuid, this.userUuid, this.nickname, this.content);
    }

    @PreUpdate // 업데이트되기 직전(UPDATE 쿼리 실행 전)에 실행
    public void preUpdate() {
        this.updatedDate = Instant.now();
        log.info("Entity is about to be updated - CommentUUID: {}, Content: {}, UpdatedDate: {}",
                this.commentUuid, this.content, this.updatedDate);
    }

    @PreRemove // 삭제되기 직전(DELETE 쿼리 실행 전)에 실행
    public void preRemove() {
        log.info("Entity is about to be removed: {}", this.commentUuid);
    }
}
