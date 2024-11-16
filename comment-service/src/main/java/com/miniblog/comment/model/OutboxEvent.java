package com.miniblog.comment.model;

import com.miniblog.comment.util.EventType;
import com.miniblog.comment.util.SagaStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "outbox_event")
@Builder
@Slf4j
public class OutboxEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String traceId;

    @Column(nullable = false)
    private String eventUuid;

    @Column(nullable = false)
    private String commentUuid;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType eventType;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String payload;
    private Instant createdDate;

    @Enumerated(EnumType.STRING)
    private SagaStatus sagaStatus; // CREATED, PROCESSING, COMPLETED, FAILED

    private Boolean processed;

    @PrePersist // 데이터베이스에 저장되기 직전(INSERT 쿼리 실행 전)에 실행
    public void prePersist() {
        if(eventUuid == null) {
            eventUuid = UUID.randomUUID().toString();
        }
        this.createdDate = Instant.now(); // 무조건 현재 시간으로 설정
        this.sagaStatus = SagaStatus.CREATED; // 항상 CREATED 상태로 초기화
        this.processed = false; // 항상 false로 초기화
        log.info("Comment is about to be created - EventUUID: {}, CommentUUID: {}, EventType: {}, CreateDate: {}, SagaStatus: {}, Processed: {}, Payload: {}",
                this.eventUuid, this.commentUuid, this.eventType, this.createdDate, this.sagaStatus, this.processed, this.payload);
    }
}
