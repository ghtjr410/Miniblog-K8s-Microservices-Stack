package com.miniblog.query.repository;

import com.miniblog.query.model.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProfileRepository extends MongoRepository<Profile, String> {
    // 사용자 UUID로 프로필을 찾는 메서드 추가
    Optional<Profile> findByProfileUuid(String profileUuid);
}
