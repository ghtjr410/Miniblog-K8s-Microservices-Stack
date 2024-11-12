package com.miniblog.profile.model;

import com.miniblog.profile.util.EventType;
import com.miniblog.profile.util.SagaStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "outbox_event")
@Builder
public class OutboxEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)// 일단 true로 tempo적용하면 false로
    private String traceId;

    @Column(nullable = false)
    private String eventUuid;

    @Column(nullable = false)
    private String profileUuid;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType eventType;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String payload;
    private Date createdDate;

    @Enumerated(EnumType.STRING)
    private SagaStatus sagaStatus; // CREATED, PROCESSING, COMPLETED, FAILED

    private Boolean processed;
}