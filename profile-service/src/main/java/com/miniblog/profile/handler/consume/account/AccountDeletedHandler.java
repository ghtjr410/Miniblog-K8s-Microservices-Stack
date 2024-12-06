package com.miniblog.profile.handler.consume.account;

import com.miniblog.account.avro.AccountDeletedEvent;
import com.miniblog.profile.handler.consume.EventConsumerHandler;
import com.miniblog.profile.repository.profile.ProfileRepository;
import com.miniblog.profile.util.ConsumedEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountDeletedHandler implements EventConsumerHandler {
    private final ProfileRepository profileRepository;

    @Override
    @Transactional
    public void handleEvent(SpecificRecordBase event) {
        AccountDeletedEvent accountDeletedEvent = (AccountDeletedEvent) event;
        UUID userUuid = UUID.fromString(accountDeletedEvent.getUserUuid().toString());
        try {
            profileRepository.deleteByUserUuid(userUuid);
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
