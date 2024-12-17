package com.miniblog.query.repository.post;

public interface PostIncrementOperations {
    boolean incrementTotalViews(String postUuid); // 조회수 증가
    boolean incrementLikeCount(String postUuid); // 좋아요 증가
    boolean incrementCommentCount(String postUuid); // 댓글 수 증가
}
