package com.miniblog.viewcount.model;

import com.miniblog.viewcount.listener.ReceivedEventListener;
import com.miniblog.viewcount.util.ReceivedEventType;
import com.miniblog.viewcount.util.SagaStatus;
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
@EntityListeners(ReceivedEventListener.class)
@Table(
        name = "received_event",
        indexes = {
        @Index(name = "idx_event_saga", columnList = "event_uuid, saga_status"),
        @Index(name = "idx_event_processed", columnList = "event_uuid, processed")
}
)
public class ReceivedEvent {

    @Id
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "event_uuid", length = 36)
    private UUID eventUuid;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 30)
    private ReceivedEventType eventType;

    @Enumerated(EnumType.STRING)
    @Column(name = "saga_status", nullable = false, length = 30)
    private SagaStatus sagaStatus; // CREATED, PROCESSING, COMPLETED, FAILED

    @CreationTimestamp
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "processed", nullable = false)
    private Boolean processed;
}
