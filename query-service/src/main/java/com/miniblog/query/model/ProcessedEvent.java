package com.miniblog.query.model;

import com.miniblog.query.util.SagaStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "processed_event")
public class ProcessedEvent {
    @Id
    private String Id;

    @Indexed(unique = true)
    private String eventUuid;

    private SagaStatus sagaStatus; // PROCESSING, COMPLETED, FAILED
}
