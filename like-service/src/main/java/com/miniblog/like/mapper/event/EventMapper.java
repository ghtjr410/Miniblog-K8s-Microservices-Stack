package com.miniblog.like.mapper.event;

import com.miniblog.like.model.Like;
import org.apache.avro.specific.SpecificRecordBase;

public interface EventMapper {
    SpecificRecordBase createToEvent(Like like);
}
