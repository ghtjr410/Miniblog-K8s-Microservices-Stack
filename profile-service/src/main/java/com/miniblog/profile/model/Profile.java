package com.miniblog.profile.model;

import com.miniblog.profile.listener.ProfileListener;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(ProfileListener.class)
@Entity
@Table(name = "profile")
public class Profile {
    @Id
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @UuidGenerator
    @Column(name = "profile_uuid", length = 36)
    private UUID profileUuid;

    // 이거 인덱싱해야하고
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "user_uuid", unique = true, nullable = false, length = 36)
    private UUID userUuid;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "title")
    private String title;

    @Column(name = "intro")
    private String intro;
}
