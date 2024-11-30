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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(CommentListener.class)
@Table(name = "comment")
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

    @Column(name = "created_date", nullable = false)
    private Instant createdDate;
    @Column(name = "updated_Date", nullable = false)
    private Instant updatedDate;

}
