package com.miniblog.post.model;

import com.miniblog.post.util.SagaStatus;
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

    @Column(nullable = false)
    private String eventUuId;

    @Column(nullable = false)
    private String postUuid;

    private String eventType;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String payload;
    private Date createdDate;

    @Enumerated(EnumType.STRING)
    private SagaStatus sagaStatus;

    private Boolean processed;
}
