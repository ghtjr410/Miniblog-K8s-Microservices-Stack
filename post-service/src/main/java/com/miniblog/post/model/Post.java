package com.miniblog.post.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "post")
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String postUuid;

    private String userUuid;
    private String nickname;
    private String title;
    private String content;
    private Date createdDate;
    private Date updatedDate;

    @PrePersist
    public void prePersist() {
        if (postUuid == null) {
            postUuid = UUID.randomUUID().toString();
        }
    }


}
