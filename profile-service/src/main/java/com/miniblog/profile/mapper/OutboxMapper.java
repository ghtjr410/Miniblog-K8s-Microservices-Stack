package com.miniblog.profile.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.miniblog.profile.avro.ProfileCreatedEvent;
import com.miniblog.profile.avro.ProfileUpdatedEvent;
import com.miniblog.profile.model.OutboxEvent;
import com.miniblog.profile.model.Profile;
import com.miniblog.profile.util.EventType;
import com.miniblog.profile.util.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.io.JsonEncoder;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxMapper {
    private final AvroJsonSerializer avroJsonSerializer;

    public OutboxEvent toOutboxEvent(Profile profile, boolean isNew) throws JsonProcessingException {
        try {
            SpecificRecordBase event;
            EventType eventType;

            if (isNew) {
                // ProfileCreatedEvent 생성
                ProfileCreatedEvent profileCreatedEvent = new ProfileCreatedEvent();
                profileCreatedEvent.setProfileUuid(profile.getProfileUuid());
                profileCreatedEvent.setUserUuid(profile.getUserUuid());
                profileCreatedEvent.setNickname(profile.getNickname());
                profileCreatedEvent.setEmail(profile.getEmail());
                profileCreatedEvent.setTitle(profile.getTitle());
                profileCreatedEvent.setIntro(profile.getIntro());
                event = profileCreatedEvent;
                eventType = EventType.PROFILE_CREATED;
            } else {
                // ProfileUpdatedEvent 생성
                ProfileUpdatedEvent profileUpdatedEvent = new ProfileUpdatedEvent();
                profileUpdatedEvent.setProfileUuid(profile.getProfileUuid());
                profileUpdatedEvent.setTitle(profile.getTitle());
                profileUpdatedEvent.setIntro(profile.getIntro());
                event = profileUpdatedEvent;
                eventType = EventType.PROFILE_UPDATED;
            }

            // Avro 객체를 JSON 문자열로 직렬화
            String payload = avroJsonSerializer.serialize(event);

            return OutboxEvent.builder()
                    .eventUuid(UUID.randomUUID().toString())
                    .profileUuid(profile.getProfileUuid())
                    .eventType(eventType)
                    .payload(payload)
                    .createdDate(new Date())
                    .sagaStatus(SagaStatus.CREATED)
                    .processed(false)
                    .build();
        } catch (IOException ex) {
            log.error("OutboxMapper toOutboxEvent : {}", ex.getMessage(), ex);
            throw new RuntimeException("OutboxEvent 생성 실패", ex);
        }
    }
}
