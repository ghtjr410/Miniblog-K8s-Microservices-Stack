package com.miniblog.query.dto;

import com.miniblog.query.model.Comment;
import com.miniblog.query.model.Post;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class PostDetailDTO {
    private Post post;
    private List<Comment> comments;
}
