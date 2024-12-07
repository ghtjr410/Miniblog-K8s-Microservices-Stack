package com.miniblog.viewcount.model;

import com.miniblog.viewcount.listener.ViewcountListener;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(ViewcountListener.class)
@Entity
@Table(
        name = "viewcount",
        indexes = {
                @Index(name = "idx_user_uuid", columnList = "user_uuid")
        }
)
public class Viewcount {
    @Id
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "post_uuid", length = 36)
    private UUID postUuid;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "user_uuid", length = 36)
    private UUID userUuid;

    @Column(name = "total_views", nullable = false)
    @Min(0)
    @Builder.Default
    private Long totalViews = 0L;

    // 낙관적 락 버전 필드 추가
    @Version
    @Column(name = "version", nullable = false)
    @Builder.Default
    private Long version = 0L;
}