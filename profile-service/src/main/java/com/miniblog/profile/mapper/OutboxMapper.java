package com.miniblog.profile.mapper;

import com.miniblog.profile.model.OutboxEvent;
import com.miniblog.profile.model.Profile;
import com.miniblog.profile.serializer.AvroJsonSerializer;
import com.miniblog.profile.util.ProducedEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxMapper {
    private final AvroJsonSerializer avroJsonSerializer;
    private final ProfileCreatedEventMapper profileCreatedEventMapper;
    private final ProfileUpdatedEventMapper profileUpdatedEventMapper;

    public OutboxEvent toOutboxEvent(Profile profile, ProducedEventType eventType) {
        SpecificRecordBase event;

        switch (eventType) {
            case PROFILE_CREATED -> event = profileCreatedEventMapper.toEntity(profile);
            case PROFILE_UPDATED -> event = profileUpdatedEventMapper.toEntity(profile);
            default -> throw new IllegalArgumentException("지원하지 않는 이벤트 타입입니다: " + eventType);
        }
        String payload = avroJsonSerializer.serialize(event);
        return OutboxEvent.builder()
                .postUuid(profile.getProfileUuid())
                .eventType(eventType)
                .payload(payload)
                .build();

    }
}

