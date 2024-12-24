package com.miniblog.account.model;

import com.miniblog.account.listener.OutboxEventListener;
import com.miniblog.account.util.ProducedEventType;
import com.miniblog.account.util.SagaStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
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
@EntityListeners(OutboxEventListener.class)
@Entity
@Table(
        name = "outbox_event",
        indexes = {
                @Index(name = "idx_saga_processed", columnList = "saga_status, processed"),
                @Index(name = "idx_event_saga", columnList = "event_uuid, saga_status")
        }
)
public class OutboxEvent {
    @Id
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @UuidGenerator
    @Column(name = "event_uuid", length = 36)
    private UUID eventUuid;

    @Column(name = "trace_id", nullable = false, length = 32)
    private String traceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 30)
    private ProducedEventType eventType;

    @Lob
    @Column(name = "payload", columnDefinition = "LONGTEXT", nullable = false)
    private String payload;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "saga_status", nullable = false, length = 30)
    private SagaStatus sagaStatus; // CREATED, PROCESSING, COMPLETED, FAILED

    @Column(name = "processed", nullable = false)
    private Boolean processed;
}