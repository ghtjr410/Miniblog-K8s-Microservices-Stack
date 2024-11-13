package com.miniblog.query.service;

import com.miniblog.query.model.Post;
import com.miniblog.query.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueryService {
    private final PostRepository postRepository;

    public Post getPostByUuid(String postUuid) {
        return postRepository.findByPostUuid(postUuid);
    }

    // 하나의 메서드에서 모든것을 처리할 수 있음
    // http://localhost:4043/api/v1/posts?page=1&size=40&createdDate,DESC
    // http://localhost:4043/api/v1/posts?sort=views,DESC
    // http://localhost:4043/api/v1/posts?sort=likes,DESC
    public Page<Post> getPosts(Pageable pageable) {
        log.info("현재위치 QueryService - getPosts :");
        return postRepository.findAll(pageable);
    }
}
