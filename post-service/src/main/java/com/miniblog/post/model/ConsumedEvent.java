package com.miniblog.post.model;

import com.miniblog.post.listener.ConsumedEventListener;
import com.miniblog.post.util.ConsumedEventType;
import com.miniblog.post.util.SagaStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(ConsumedEventListener.class)
@Entity
@Table(
        name = "consumed_event",
        indexes = {
                @Index(name = "idx_processed_event", columnList = "processed, event_uuid"),
                @Index(name = "idx_event_saga", columnList = "event_uuid, saga_status")
        }
)
public class ConsumedEvent {
    @Id
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "event_uuid", length = 36)
    private UUID eventUuid;

    @Column(name = "trace_id", nullable = false, length = 32)
    private String traceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 30)
    private ConsumedEventType eventType;

    @Enumerated(EnumType.STRING)
    @Column(name = "saga_status", nullable = false, length = 30)
    private SagaStatus sagaStatus;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate;

    @Column(name = "processed", nullable = false)
    private Boolean processed;
}
