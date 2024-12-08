package com.miniblog.like.service.like;

import com.miniblog.like.dto.LikeResponseDTO;
import com.miniblog.like.dto.ToggleLikeResult;
import com.miniblog.like.mapper.like.LikeMapper;
import com.miniblog.like.model.Like;
import com.miniblog.like.repository.like.LikeRepository;

import com.miniblog.like.service.outbox.OutboxEventService;
import com.miniblog.like.util.ProducedEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final LikeMapper likeMapper;
    private final OutboxEventService outboxEventService;

    @Transactional
    public ToggleLikeResult toggleLike(String userUuid, String postUuid) {
        UUID postUuidAsUUID = UUID.fromString(postUuid);
        UUID userUuidAsUUID = UUID.fromString(userUuid);
        Optional<Like> optionalLike = likeRepository.findByPostUuidAndUserUuid(postUuidAsUUID, userUuidAsUUID);
        if(optionalLike.isPresent()) {
            // 좋아요 삭제
            Like existingLike = optionalLike.get();
            likeRepository.delete(existingLike);
            outboxEventService.createOutboxEvent(existingLike, ProducedEventType.LIKE_DELETED);

            return buildToggleLikeResult(existingLike, false);
        } else {
            // 좋아요 추가
            Like newLike = likeMapper.createToEntity(userUuidAsUUID, postUuidAsUUID);
            likeRepository.save(newLike);
            outboxEventService.createOutboxEvent(newLike, ProducedEventType.LIKE_CREATED);

            return buildToggleLikeResult(newLike, true);
        }
    }

    private ToggleLikeResult buildToggleLikeResult(Like like, boolean isLiked) {
        LikeResponseDTO likeResponseDTO = likeMapper.toResponseDTO(like);
        return new ToggleLikeResult(likeResponseDTO, isLiked);
    }
}
