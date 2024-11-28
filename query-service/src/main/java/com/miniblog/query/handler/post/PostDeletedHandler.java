package com.miniblog.query.handler.post;

import com.miniblog.post.avro.PostDeletedEvent;
import com.miniblog.query.handler.EventConsumerHandler;
import com.miniblog.query.repository.comment.CommentRepository;
import com.miniblog.query.repository.like.LikeRepository;
import com.miniblog.query.repository.post.PostRepository;
import com.miniblog.query.util.ConsumedEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostDeletedHandler implements EventConsumerHandler {
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    @Override
    public void handleEvent(SpecificRecordBase event) {
        PostDeletedEvent postDeletedEvent = (PostDeletedEvent) event;
        String postUuid = postDeletedEvent.getPostUuid().toString();
        try {
            // 댓글 삭제
            commentRepository.deleteByPostUuid(postUuid);
            // 좋아요 삭제
            likeRepository.deleteByPostUuid(postUuid);
            // 게시물 삭제
            postRepository.deleteById(postUuid);

            log.info("게시물 및 연관 데이터 삭제 성공: postUuid={}", postUuid);
        } catch (DataAccessException ex) {
            log.error("데이터 삭제 중 예외 발생: postUuid={}, error={}", postUuid, ex.getMessage());
            // 예외를 던져서 재시도가 이루어지도록 함
            throw ex;
        } catch (Exception ex) {
            log.error("알 수 없는 예외 발생: postUuid={}, error={}", postUuid, ex.getMessage());
            throw ex;
        }
    }

    @Override
    public ConsumedEventType getEventType() {
        return ConsumedEventType.POST_DELETE;
    }
}
