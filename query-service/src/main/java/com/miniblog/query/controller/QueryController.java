package com.miniblog.query.controller;

import com.miniblog.query.model.Post;
import com.miniblog.query.repository.PostRepository;
import com.miniblog.query.service.QueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Slf4j
public class QueryController {
    private final QueryService queryService;
    private final PostRepository postRepository;

    @GetMapping("/{postUuid}")
    public Post getPostByUuid(@PathVariable String postUuid) {
        return queryService.getPostByUuid(postUuid);
    }

    @GetMapping
    public Page<Post> getPosts(Pageable pageable) {
//        try{
//            Thread.sleep(5000);
//        }catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        log.info("현재위치 QueryController - getPosts :");
        return queryService.getPosts(pageable);
    }
}
