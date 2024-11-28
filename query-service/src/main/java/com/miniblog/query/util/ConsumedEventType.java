package com.miniblog.query.util;

import com.miniblog.account.avro.AccountDeletedEvent;
import com.miniblog.comment.avro.CommentCreatedEvent;
import com.miniblog.comment.avro.CommentDeletedEvent;
import com.miniblog.comment.avro.CommentUpdatedEvent;
import com.miniblog.like.avro.LikeCreatedEvent;
import com.miniblog.like.avro.LikeDeletedEvent;
import com.miniblog.post.avro.PostCreatedEvent;
import com.miniblog.post.avro.PostDeletedEvent;
import com.miniblog.post.avro.PostUpdatedEvent;
import com.miniblog.profile.avro.ProfileCreatedEvent;
import com.miniblog.profile.avro.ProfileUpdatedEvent;
import com.miniblog.viewcount.avro.ViewcountUpdatedEvent;
import lombok.Getter;
import org.apache.avro.specific.SpecificRecordBase;

@Getter
public enum ConsumedEventType {
    POST_CREATE(PostCreatedEvent.class),
    POST_UPDATE(PostUpdatedEvent.class),
    POST_DELETE(PostDeletedEvent.class),
    COMMENT_CREATE(CommentCreatedEvent.class),
    COMMENT_UPDATE(CommentUpdatedEvent.class),
    COMMENT_DELETE(CommentDeletedEvent.class),
    LIKE_CREATE(LikeCreatedEvent.class),
    LIKE_DELETE(LikeDeletedEvent.class),
    VIEWCOUNT_UPDATE(ViewcountUpdatedEvent.class),
    PROFILE_CREATE(ProfileCreatedEvent.class),
    PROFILE_UPDATE(ProfileUpdatedEvent.class),
    ACCOUNT_DELETE(AccountDeletedEvent.class);

    private final Class<? extends SpecificRecordBase> eventClass;

    ConsumedEventType(Class<? extends SpecificRecordBase> eventClass) {
        this.eventClass = eventClass;
    }
}
