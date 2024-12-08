package com.miniblog.viewcount.mapper.event;

import com.miniblog.viewcount.model.Viewcount;
import org.apache.avro.specific.SpecificRecordBase;

public interface EventMapper {
    SpecificRecordBase createToEvent(Viewcount viewcount);
}
