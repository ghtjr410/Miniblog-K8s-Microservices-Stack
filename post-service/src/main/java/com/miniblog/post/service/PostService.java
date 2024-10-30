package com.miniblog.post.service;

import com.miniblog.post.dto.PostRequestDTO;
import com.miniblog.post.dto.PostResponseDTO;
import com.miniblog.post.mapper.OutboxMapper;
import com.miniblog.post.mapper.PostMapper;
import com.miniblog.post.model.OutboxEvent;
import com.miniblog.post.model.Post;
import com.miniblog.post.repository.OutboxEventRepository;
import com.miniblog.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;

    private final PostMapper postMapper;
    @Transactional
    public ResponseEntity<PostResponseDTO> createPost(
            String userUuid,
            PostRequestDTO postRequestDTO) {

        Post post = postMapper.toEntity(userUuid, postRequestDTO);
        post = postRepository.save(post);
        PostResponseDTO postResponseDTO = postMapper.toResponseDTO(post);

        log.info("Success Create Post : id = {}, postUuid = {} ", post.getId(), post.getPostUuid());
        return ResponseEntity.status(HttpStatus.CREATED).body(postResponseDTO);
    }
}
