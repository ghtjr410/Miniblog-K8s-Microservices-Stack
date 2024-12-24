package com.miniblog.query.handler.account;

import com.miniblog.account.avro.AccountDeletedEvent;
import com.miniblog.query.handler.EventConsumerHandler;
import com.miniblog.query.model.Comment;
import com.miniblog.query.model.Like;
import com.miniblog.query.repository.comment.CommentRepository;
import com.miniblog.query.repository.like.LikeRepository;
import com.miniblog.query.repository.post.PostRepository;
import com.miniblog.query.repository.profile.ProfileRepository;
import com.miniblog.query.util.ConsumedEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountDeletedHandler implements EventConsumerHandler {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final ProfileRepository profileRepository;

    @Override
    public void handleEvent(SpecificRecordBase event) {
        AccountDeletedEvent accountDeletedEvent = (AccountDeletedEvent) event;
        String userUuid = accountDeletedEvent.getUserUuid().toString();
        try {
            // 1. 유저가 작성한 댓글과 좋아요를 먼저 조회한다.
            List<Comment> userComments = commentRepository.findByUserUuid(userUuid);
            List<Like> userLikes = likeRepository.findByUserUuid(userUuid);

            // 2. 유저가 남긴 댓글을 달았던 게시글의 commentCount를 1씩 감소시킨다.
            for (Comment comment : userComments) {
                postRepository.decrementCommentCount(comment.getPostUuid());
            }

            // 3. 유저가 좋아요를 눌렀던 게시글의 likeCount를 1씩 감소시킨다.
            for (Like like : userLikes) {
                postRepository.decrementLikeCount(like.getPostUuid());
            }

            // 4. 유저가 작성한 게시글(본인 소유), 댓글, 좋아요, 프로필 정보를 모두 삭제한다
            postRepository.deleteByUserUuid(userUuid);
            commentRepository.deleteByUserUuid(userUuid);
            likeRepository.deleteByUserUuid(userUuid);
            profileRepository.deleteByUserUuid(userUuid);

            log.info("게시물 및 연관 데이터 삭제 성공: userUuid={}", userUuid);
        } catch (DataAccessException ex) {
            log.error("데이터 삭제 중 예외 발생: userUuid={}, error={}", userUuid, ex.getMessage());
            // 예외를 던져서 재시도가 이루어지도록 함
            throw ex;
        } catch (Exception ex) {
            log.error("알 수 없는 예외 발생: userUuid={}, error={}", userUuid, ex.getMessage());
            throw ex;
        }
    }

    @Override
    public ConsumedEventType getEventType() {
        return ConsumedEventType.ACCOUNT_DELETE;
    }
}
