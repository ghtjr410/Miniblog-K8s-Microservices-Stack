package com.miniblog.viewcount.mapper;

import com.miniblog.viewcount.avro.ViewcountUpdatedEvent;
import com.miniblog.viewcount.model.Viewcount;
import org.springframework.stereotype.Component;

@Component
public class ViewcountUpdatedEventMapper {
    public ViewcountUpdatedEvent toEntity(Viewcount viewcount) {
        ViewcountUpdatedEvent viewcountUpdatedEvent = new ViewcountUpdatedEvent();
        viewcountUpdatedEvent.setPostUuid(viewcount.getPostUuid().toString());
        viewcountUpdatedEvent.setTotalViews(viewcount.getTotalViews());
        return viewcountUpdatedEvent;
    }
}
