package com.miniblog.profile.mapper;

import com.miniblog.profile.avro.ProfileCreatedEvent;
import com.miniblog.profile.model.Profile;
import org.springframework.stereotype.Component;

@Component
public class ProfileCreatedEventMapper {
    public ProfileCreatedEvent toEntity(Profile profile) {
        ProfileCreatedEvent profileCreatedEvent = new ProfileCreatedEvent();
        profileCreatedEvent.setProfileUuid(profile.getProfileUuid().toString());
        profileCreatedEvent.setUserUuid(profile.getNickname());
        profileCreatedEvent.setNickname(profile.getNickname());
        profileCreatedEvent.setEmail(profile.getEmail());
        profileCreatedEvent.setTitle(profile.getTitle());
        profileCreatedEvent.setIntro(profile.getIntro());
        return profileCreatedEvent;
    }
}
