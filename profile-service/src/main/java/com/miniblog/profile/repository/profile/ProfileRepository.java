package com.miniblog.profile.repository.profile;

import com.miniblog.profile.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, UUID> {
    // 사용자 UUID로 프로필을 찾는 메서드 추가
    Optional<Profile> findByUserUuid(UUID userUuid);
    void deleteByUserUuid(UUID userUuid);
}
