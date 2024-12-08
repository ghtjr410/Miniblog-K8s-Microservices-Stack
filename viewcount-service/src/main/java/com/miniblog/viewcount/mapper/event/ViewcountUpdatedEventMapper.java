package com.miniblog.viewcount.mapper.event;

import com.miniblog.viewcount.avro.ViewcountUpdatedEvent;
import com.miniblog.viewcount.model.Viewcount;
import org.springframework.stereotype.Component;

@Component
public class ViewcountUpdatedEventMapper implements EventMapper {
    @Override
    public ViewcountUpdatedEvent createToEvent(Viewcount viewcount) {
        ViewcountUpdatedEvent viewcountUpdatedEvent = new ViewcountUpdatedEvent();
        viewcountUpdatedEvent.setPostUuid(viewcount.getPostUuid().toString());
        viewcountUpdatedEvent.setTotalViews(viewcount.getTotalViews());
        return viewcountUpdatedEvent;
    }
}
