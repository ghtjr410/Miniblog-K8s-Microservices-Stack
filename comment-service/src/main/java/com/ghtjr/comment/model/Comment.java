package com.ghtjr.comment.model;

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
@Table(name = "comment")
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String commentUuid;

    @Column(nullable = false)
    private String userUuid;

    @Column(nullable = false)
    private String nickname;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;
    private Date createdDate;
    private Date updatedDate;

    @PrePersist
    public void prePersist() {
        if(commentUuid == null) {
            commentUuid = UUID.randomUUID().toString();
        }
    }
}
