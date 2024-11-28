package com.miniblog.query.mapper;

import com.miniblog.profile.avro.ProfileCreatedEvent;
import com.miniblog.profile.avro.ProfileUpdatedEvent;
import com.miniblog.query.model.Profile;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {
    public Profile toCreatedProfile(ProfileCreatedEvent profileCreatedEvent) {
        return Profile.builder()
                .profileUuid(profileCreatedEvent.getProfileUuid().toString())
                .userUuid(profileCreatedEvent.getUserUuid().toString())
                .nickname(profileCreatedEvent.getNickname().toString())
                .email(profileCreatedEvent.getEmail().toString())
                .title(profileCreatedEvent.getTitle().toString())
                .intro(profileCreatedEvent.getIntro().toString())
                .build();
    }
}
