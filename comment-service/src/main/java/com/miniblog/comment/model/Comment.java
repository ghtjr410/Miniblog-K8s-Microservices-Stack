package com.miniblog.comment.model;

import com.miniblog.comment.listener.CommentListener;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(CommentListener.class)
@Entity
@Table(
        name = "comment",
        indexes = {
                @Index(name = "idx_post_uuid", columnList = "post_uuid"), // postUuid에 대한 인덱스 추가
                @Index(name = "idx_user_uuid", columnList = "user_uuid"), // userUuid에 대한 인덱스 추가
        }
)
public class Comment {
    @Id
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @UuidGenerator
    @Column(name = "comment_uuid", length = 36)
    private UUID commentUuid;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "post_uuid", nullable = false, length = 36)
    private UUID postUuid;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "user_uuid", nullable = false, length = 36)
    private UUID userUuid;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Lob
    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate;

    @Column(name = "updated_date", nullable = false)
    private Instant updatedDate;
}
