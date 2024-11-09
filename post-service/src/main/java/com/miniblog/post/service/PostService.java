package com.miniblog.post.service;

import com.miniblog.post.dto.PostRequestDTO;
import com.miniblog.post.dto.PostResponseDTO;
import com.miniblog.post.mapper.OutboxMapper;
import com.miniblog.post.mapper.PostMapper;
import com.miniblog.post.model.OutboxEvent;
import com.miniblog.post.model.Post;
import com.miniblog.post.repository.OutboxEventRepository;
import com.miniblog.post.repository.PostRepository;
import io.micrometer.tracing.Tracer;
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
    private final OutboxEventRepository outboxEventRepository;
    private final PostMapper postMapper;
    private final OutboxMapper outboxMapper;
    private final Tracer tracer;

    @Transactional
    public ResponseEntity<PostResponseDTO> createPost(
            String userUuid,
            PostRequestDTO postRequestDTO) {
        try {
            Post post = postMapper.toEntity(userUuid, postRequestDTO);
            post = postRepository.save(post);

            OutboxEvent outboxEvent = outboxMapper.toEntity(post);

            String traceId = tracer.currentSpan().context().traceId();
            outboxEvent.setTraceId(traceId);

            outboxEventRepository.save(outboxEvent);
//            if (true) {
//                // 강제 예외 발생
//                throw new RuntimeException("Simulated exception during Kafka send for testing.");
//            }
            PostResponseDTO postResponseDTO = postMapper.toResponseDTO(post);
            log.info("Success Create Post : id = {}, postUuid = {} ", post.getId(), post.getPostUuid());

            return ResponseEntity.status(HttpStatus.CREATED).body(postResponseDTO);
        } catch (Exception e) {
            log.error("Error Create Post : ", e);
            // 예외를 서비스계층에서 벗어나게 하여 트랜잭션이 롤백되도록함
            throw new RuntimeException("Error occurred while creating post", e);
        }
    }
}
