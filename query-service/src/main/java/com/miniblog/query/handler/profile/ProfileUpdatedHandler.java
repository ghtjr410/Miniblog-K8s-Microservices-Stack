package com.miniblog.query.handler.profile;

import com.miniblog.profile.avro.ProfileUpdatedEvent;
import com.miniblog.query.handler.EventConsumerHandler;
import com.miniblog.query.repository.profile.ProfileRepository;
import com.miniblog.query.util.ConsumedEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileUpdatedHandler implements EventConsumerHandler {
    private final ProfileRepository profileRepository;

    @Override
    public void handleEvent(SpecificRecordBase event) {
        ProfileUpdatedEvent profileUpdatedEvent = (ProfileUpdatedEvent) event;
        String profileUuid = profileUpdatedEvent.getProfileUuid().toString();
        String title = profileUpdatedEvent.getTitle().toString();
        String intro = profileUpdatedEvent.getIntro().toString();

        profileRepository.updateProfile(profileUuid, title, intro);
    }

    @Override
    public ConsumedEventType getEventType() {
        return ConsumedEventType.PROFILE_UPDATE;
    }
}
