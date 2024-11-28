package com.miniblog.query.model;

import com.miniblog.query.util.ConsumedEventType;
import com.miniblog.query.util.SagaStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@Document(collection = "consumed_event")
@CompoundIndex(name = "eventUuid_sagaStatus_idx", def = "{'eventUuid': 1, 'sagaStatus': 1}")
public class ConsumedEvent {
    @Id
    private String eventUuid;

    private ConsumedEventType eventType;

    private SagaStatus sagaStatus; // PROCESSING, COMPLETED, FAILED

    private Instant createdDate;

    private Boolean processed;
}
