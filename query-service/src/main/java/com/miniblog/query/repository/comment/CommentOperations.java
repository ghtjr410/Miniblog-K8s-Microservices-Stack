package com.miniblog.query.repository.comment;

import java.time.Instant;

public interface CommentOperations {
    boolean updateComment(String commentUuid, String content, Instant updatedDate);
}
