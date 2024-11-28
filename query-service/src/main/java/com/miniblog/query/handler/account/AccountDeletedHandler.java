package com.miniblog.query.handler.account;

import com.miniblog.account.avro.AccountDeletedEvent;
import com.miniblog.query.handler.EventConsumerHandler;
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
            // 게시글 삭제
            postRepository.deleteByUserUuid(userUuid);
            // 댓글 삭제
            commentRepository.deleteByUserUuid(userUuid);
            // 좋아요 삭제
            likeRepository.deleteByUserUuid(userUuid);
            // 프로필 삭제
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
