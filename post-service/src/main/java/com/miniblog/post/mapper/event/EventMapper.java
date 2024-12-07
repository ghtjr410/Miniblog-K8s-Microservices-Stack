package com.miniblog.post.mapper.event;

import com.miniblog.post.model.Post;
import org.apache.avro.specific.SpecificRecordBase;

public interface EventMapper {
    SpecificRecordBase createToEvent(Post post);
}
