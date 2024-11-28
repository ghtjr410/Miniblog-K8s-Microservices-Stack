package com.miniblog.query.repository.profile;

import com.miniblog.query.model.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProfileRepository extends MongoRepository<Profile, String>, ProfileOperations {
    Optional<Profile> findByUserUuid(String userUuid);
    void deleteByUserUuid(String userUuid);
}
