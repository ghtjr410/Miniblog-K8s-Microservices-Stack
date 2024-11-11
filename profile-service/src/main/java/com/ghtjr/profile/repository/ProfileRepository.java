package com.ghtjr.profile.repository;

import com.ghtjr.profile.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    // 사용자 UUID로 프로필을 찾는 메서드 추가
    Optional<Profile> findByUserUuid(String userUuid);
}
