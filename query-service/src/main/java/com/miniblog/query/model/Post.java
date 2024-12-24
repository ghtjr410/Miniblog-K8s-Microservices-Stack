package com.miniblog.query.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@Document(collection = "posts")
@CompoundIndexes({
        // 닉네임 + 작성일 내림차순 정렬용
        @CompoundIndex(name = "nickname_createdDate_idx", def = "{'nickname': 1, 'createdDate': -1}"),
        // 닉네임 + 좋아요수 내림차순 정렬용
        @CompoundIndex(name = "nickname_likeCount_idx", def = "{'nickname': 1, 'likeCount': -1}"),
        // 닉네임 + 조회수 내림차순 정렬용
        @CompoundIndex(name = "nickname_totalViews_idx", def = "{'nickname': 1, 'totalViews': -1}")
})
public class Post {
    @Id
    private String postUuid;

    @Indexed
    private String userUuid;

    @Indexed
    private String nickname;

    @TextIndexed
    private String title;

    @TextIndexed
    private String plainContent; // HTML 제거된 순수 텍스트만 저장
    private String content;      // HTML이 포함된 원본 (에디터 렌더링용)

    @Indexed(direction = IndexDirection.DESCENDING)
    private Instant createdDate;
    private Instant updatedDate;

    // 집계 필드
    private Integer likeCount;
    private Integer totalViews;
    private Integer commentCount;
}
