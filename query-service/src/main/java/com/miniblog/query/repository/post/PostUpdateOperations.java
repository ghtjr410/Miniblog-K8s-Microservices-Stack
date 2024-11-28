package com.miniblog.query.repository.post;

import java.time.Instant;

public interface PostUpdateOperations {
    public boolean updatePost(String postUuid, String title, String content, Instant updatedDate);
}
