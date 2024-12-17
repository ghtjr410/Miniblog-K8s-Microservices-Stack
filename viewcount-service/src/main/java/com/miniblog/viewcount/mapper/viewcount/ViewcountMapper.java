package com.miniblog.viewcount.mapper.viewcount;

import com.miniblog.viewcount.model.Viewcount;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ViewcountMapper {
    public Viewcount createToEntity(UUID postUuid, UUID userUuid) {
        return Viewcount.builder()
                .postUuid(postUuid)
                .userUuid(userUuid)
                .build();
    }
}
