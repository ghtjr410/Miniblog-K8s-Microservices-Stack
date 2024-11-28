package com.miniblog.query.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@Document(collection = "post")
@CompoundIndexes({
        @CompoundIndex(name = "user_createdDate_idx", def = "{'userUuid': 1, 'createdDate': -1}"),
        @CompoundIndex(name = "user_likeCount_idx", def = "{'userUuid': 1, 'likeCount': -1}"),
        @CompoundIndex(name = "user_viewCount_idx", def = "{'userUuid': 1, 'viewCount': -1}")
})
public class Post {
    @Id
    private String postUuid;
    @Indexed
    private String userUuid;

    private String nickname;
    @TextIndexed
    private String title;
    @TextIndexed
    private String content;

    @Indexed(direction = IndexDirection.DESCENDING)
    private Instant createdDate;
    private Instant updatedDate;

    // 집계 필드
    private Integer likeCount;
    private Integer viewCount;
    private Integer commentCount;
}
