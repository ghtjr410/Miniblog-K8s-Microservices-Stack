package com.miniblog.viewcount.mapper;

import com.miniblog.viewcount.model.Viewcount;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ViewcountMapper {
    public Viewcount createToEntity(UUID postUuid) { // 이거 아니다 이벤트로 받을거야이건
        return Viewcount.builder()
                .postUuid(postUuid)
                .build();
    }
    
    public Viewcount updateToEntity(UUID postUuid, Long totalViews) {
        return Viewcount.builder()
                .postUuid(postUuid)
                .totalViews(totalViews)
                .build();
    }
}
