package com.miniblog.query.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "post")
public class Post {
    @Id
    private String id;

    @Indexed(unique = true)
    private String postUuid;
    private String userUuid;
    private String nickname;
    private String title;
    private String content;

    private Long createdDate;
    private Long updatedDate;

    // 집계 필드
    private Integer likeCount;
    private Integer viewCount;
    private Integer commentCount;
}
