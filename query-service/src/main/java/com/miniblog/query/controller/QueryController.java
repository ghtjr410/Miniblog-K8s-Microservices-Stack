package com.miniblog.query.controller;

import com.miniblog.query.model.Post;
import com.miniblog.query.repository.PostRepository;
import com.miniblog.query.service.QueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class QueryController {
    private final QueryService queryService;
    private final PostRepository postRepository;

    @GetMapping("/{postUuid}")
    public Post getPostByUuid(@PathVariable String postUuid) {
        return queryService.getPostByUuid(postUuid);
    }

    @GetMapping
    public Page<Post> getPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }
}
