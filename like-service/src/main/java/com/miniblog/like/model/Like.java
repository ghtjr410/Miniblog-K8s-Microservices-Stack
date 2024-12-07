package com.miniblog.like.model;

import com.miniblog.like.listener.LikeListener;
import jakarta.persistence.*;
import lombok.*;
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
@EntityListeners(LikeListener.class)
@Entity
@Table(
        name = "likes",
        indexes = {
                @Index(name = "idx_post_uuid", columnList = "post_uuid"), // postUuid에 대한 인덱스 추가
                @Index(name = "idx_user_uuid", columnList = "user_uuid"), // userUuid에 대한 인덱스 추가
                @Index(name = "idx_post_user", columnList = "post_uuid, user_uuid") // postUuid, userUuid에 대한 복합인덱스 추가
        }
)
public class Like {

    @Id
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @UuidGenerator
    @Column(name = "like_uuid", length = 36)
    private UUID likeUuid;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "post_uuid", nullable = false, length = 36)
    private UUID postUuid;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "user_uuid", nullable = false, length = 36)
    private UUID userUuid;

    @Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate;
}
