package com.miniblog.query.handler.profile;

import com.miniblog.profile.avro.ProfileCreatedEvent;
import com.miniblog.query.handler.EventConsumerHandler;
import com.miniblog.query.mapper.ProfileMapper;
import com.miniblog.query.model.Profile;
import com.miniblog.query.repository.profile.ProfileRepository;
import com.miniblog.query.util.ConsumedEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileCreatedHandler implements EventConsumerHandler {
    private final ProfileMapper profileMapper;
    private final ProfileRepository profileRepository;

    @Override
    public void handleEvent(SpecificRecordBase event) {
        ProfileCreatedEvent profileCreatedEvent = (ProfileCreatedEvent) event;
        String profileUuid = profileCreatedEvent.getProfileUuid().toString();

        if (profileRepository.existsById(profileUuid)) {
            log.info("이미 처리된 프로필 생성 이벤트: profileUuid={}", profileUuid);
            return;
        }
        Profile profile = profileMapper.toCreatedProfile(profileCreatedEvent);
        try {
            profileRepository.save(profile);
        } catch (DuplicateKeyException ex) {
            log.info("중복된 프로필 저장 시도: profileUuid={}, error={}", profileUuid, ex.getMessage());
            // 이미 저장된 경우 아무 작업도 하지 않음
        }
    }

    @Override
    public ConsumedEventType getEventType() {
        return ConsumedEventType.PROFILE_CREATE;
    }
}
