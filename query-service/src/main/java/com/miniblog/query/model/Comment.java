package com.miniblog.query.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@CompoundIndexes({
        @CompoundIndex(name = "post_created_idx", def = "{'postUuid': 1, 'createdDate': -1}")
})
@Document(collection = "comments")
public class Comment {
    @Id
    private String commentUuid;
    @Indexed
    private String postUuid;
    @Indexed
    private String userUuid;

    private String nickname;
    private String content;

    @Indexed(direction = IndexDirection.ASCENDING)
    private Instant createdDate;
    private Instant updatedDate;
}
