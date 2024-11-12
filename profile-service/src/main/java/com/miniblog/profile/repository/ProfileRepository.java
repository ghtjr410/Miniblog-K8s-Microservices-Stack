package com.miniblog.profile.repository;

import com.miniblog.profile.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    // 사용자 UUID로 프로필을 찾는 메서드 추가
    Optional<Profile> findByUserUuid(String userUuid);
}
