package com.miniblog.profile.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "profile")
@Builder
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String profileUuid;

    @Column(unique = true, nullable = false)
    private String userUuid;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

    private String title;
    private String intro;

    @PrePersist
    public void prePersist() {
        if (profileUuid == null) {
            profileUuid = UUID.randomUUID().toString();
        }
    }
}
