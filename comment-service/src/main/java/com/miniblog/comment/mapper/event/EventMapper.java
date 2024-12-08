package com.miniblog.comment.mapper.event;

import com.miniblog.comment.model.Comment;
import org.apache.avro.specific.SpecificRecordBase;

public interface EventMapper {
    SpecificRecordBase createToEvent(Comment comment);
}