package com.miniblog.query.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "profiles")
public class Profile {
    @Id
    private String profileUuid;

    @Indexed
    private String userUuid;
    private String nickname;
    private String email;
    private String title;
    private String intro;
}
