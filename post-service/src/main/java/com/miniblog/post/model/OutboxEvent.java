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

    private String eventUuId;
    private String postUuid;
    private String eventType;
    private String payload;
    private Date createdDate;

    @Enumerated(EnumType.STRING)
    private SagaStatus sagaStatus;

    private Boolean processed;
}
