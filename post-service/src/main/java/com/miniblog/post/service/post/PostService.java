package com.miniblog.post.service.post;

import com.miniblog.post.dto.PostCreateRequestDTO;
import com.miniblog.post.dto.PostResponseDTO;
import com.miniblog.post.dto.PostUpdateRequestDTO;
import com.miniblog.post.exception.NotFoundException;
import com.miniblog.post.exception.UnauthorizedException;
import com.miniblog.post.mapper.PostMapper;
import com.miniblog.post.model.Post;
import com.miniblog.post.repository.post.PostRepository;
import com.miniblog.post.service.outbox.OutboxEventService;
import com.miniblog.post.util.ProducedEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final OutboxEventService outboxEventService;

    @Transactional
    public PostResponseDTO createPost(
            String userUuid,
            PostCreateRequestDTO postRequestDTO) {
        Post post = postMapper.createToEntity(userUuid, postRequestDTO);
        postRepository.save(post);
        outboxEventService.createOutboxEvent(post, ProducedEventType.POST_CREATE);
        return postMapper.toResponseDTO(post);
    }

    @Transactional
    public PostResponseDTO updatePost(
            String userUuid,
            String postUuid,
            PostUpdateRequestDTO postUpdateRequestDTO) {
        UUID postUuidAsUUID = UUID.fromString(postUuid);
        UUID userUuidAsUUID = UUID.fromString(userUuid);
        // 게시글 조회
        Post post = postRepository.findById(postUuidAsUUID)
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다: postUuid = " + postUuid));
        // 권한 확인
        if (!post.getUserUuid().equals(userUuidAsUUID)) {
            throw new UnauthorizedException("이 게시글을 수정할 권한이 없습니다: userUuid = " + userUuid);
        }
        postMapper.updateToEntity(post, postUpdateRequestDTO);
        postRepository.save(post); // 저장 시 버전 체크 (옵티미스틱 락)

        outboxEventService.createOutboxEvent(post, ProducedEventType.POST_UPDATE);
        return postMapper.toResponseDTO(post);
    }

    @Transactional
    public void deletePost(
            String userUuid,
            String postUuid) {
        UUID postUuidAsUUID = UUID.fromString(postUuid);
        UUID userUuidAsUUID = UUID.fromString(userUuid);
        // 게시글 조회
        Post post = postRepository.findById(postUuidAsUUID)
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다: postUuid = " + postUuid));
        // 권한 확인
        if (!post.getUserUuid().equals(userUuidAsUUID)) {
            throw new UnauthorizedException("이 게시글을 삭제할 권한이 없습니다: userUuid = " + userUuid);
        }
        postRepository.delete(post);

        outboxEventService.createOutboxEvent(post, ProducedEventType.POST_DELETE);
    }
}
