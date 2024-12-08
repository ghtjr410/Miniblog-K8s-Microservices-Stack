package com.miniblog.profile.mapper.event;

import com.miniblog.profile.model.Profile;
import org.apache.avro.specific.SpecificRecordBase;

public interface EventMapper {
    SpecificRecordBase createToEvent(Profile profile);
}
