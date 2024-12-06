package com.miniblog.profile.mapper;

import com.miniblog.profile.avro.ProfileUpdatedEvent;
import com.miniblog.profile.model.Profile;
import org.springframework.stereotype.Component;

@Component
public class ProfileUpdatedEventMapper {
    public ProfileUpdatedEvent toEntity(Profile profile) {
        ProfileUpdatedEvent profileUpdatedEvent = new ProfileUpdatedEvent();
        profileUpdatedEvent.setProfileUuid(profile.getProfileUuid().toString());
        profileUpdatedEvent.setTitle(profile.getTitle());
        profileUpdatedEvent.setIntro(profile.getIntro());
        return profileUpdatedEvent;
    }
}
