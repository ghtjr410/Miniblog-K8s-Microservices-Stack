package com.miniblog.profile.mapper.event;

import com.miniblog.profile.avro.ProfileUpdatedEvent;
import com.miniblog.profile.model.Profile;
import org.springframework.stereotype.Component;

@Component
public class ProfileUpdatedEventMapper implements EventMapper {
    @Override
    public ProfileUpdatedEvent createToEvent(Profile profile) {
        ProfileUpdatedEvent profileUpdatedEvent = new ProfileUpdatedEvent();
        profileUpdatedEvent.setProfileUuid(profile.getProfileUuid().toString());
        profileUpdatedEvent.setTitle(profile.getTitle());
        profileUpdatedEvent.setIntro(profile.getIntro());
        return profileUpdatedEvent;
    }
}
