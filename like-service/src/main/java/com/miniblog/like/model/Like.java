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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(LikeListener.class)
@Table(name = "likes")
public class Like {

    @Id
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @UuidGenerator
    @Column(name = "post_uuid", length = 36)
    private UUID likeUuid;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "post_uuid", nullable = false, length = 36)
    private UUID postUuid;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "user_uuid", nullable = false, length = 36)
    private UUID userUuid;

    @Column(name = "created_date", nullable = false)
    private Instant createdDate;
}
