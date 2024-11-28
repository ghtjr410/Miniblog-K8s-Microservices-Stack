package com.miniblog.query.repository.post;

public interface PostDecrementOperations {
    boolean decrementLikeCount(String postUuid); // 좋아요 감소
    boolean decrementCommentCount(String postUuid); // 댓글 수 감소
}
