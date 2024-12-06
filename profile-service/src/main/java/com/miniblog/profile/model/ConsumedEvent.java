package com.miniblog.profile.model;

import com.miniblog.profile.listener.ConsumedEventListener;
import com.miniblog.profile.util.ConsumedEventType;
import com.miniblog.profile.util.SagaStatus;
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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(ConsumedEventListener.class)
@Table(
        name = "consumed_event",
        indexes = {
                @Index(name = "idx_event_saga", columnList = "eventUuid, sagaStatus")
        }
)
public class ConsumedEvent {
    @Id
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "event_uuid", length = 36)
    private UUID eventUuid;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 30)
    private ConsumedEventType eventType;

    @Enumerated(EnumType.STRING)
    @Column(name = "saga_status", nullable = false, length = 30)
    private SagaStatus sagaStatus;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "processed", nullable = false)
    private Boolean processed;
}