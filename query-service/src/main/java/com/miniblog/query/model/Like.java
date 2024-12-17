package com.miniblog.query.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@Document(collection = "post_like")
@CompoundIndex(name = "postUuid_userUuid_idx", def = "{'postUuid': 1, 'userUuid': 1}")
public class Like {
    @Id
    private String likeUuid;

    @Indexed
    private String postUuid;

    @Indexed
    private String userUuid;

    @Indexed(direction = IndexDirection.ASCENDING)
    private Instant createdDate;
}
