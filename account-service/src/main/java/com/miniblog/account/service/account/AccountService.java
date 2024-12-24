package com.miniblog.account.service.account;

import com.miniblog.account.service.keycloak.KeycloakService;
import com.miniblog.account.service.outbox.OutboxEventService;
import com.miniblog.account.util.ProducedEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    private final KeycloakService keycloakService;
    private final OutboxEventService outboxEventService;

    @Transactional
    public void deleteUser(String userId) {
        log.info("[deleteUser] Start - userId: {}", userId);
        // 1) Outbox 이벤트 생성 (트랜잭션 내)
        outboxEventService.createOutboxEvent(userId, ProducedEventType.ACCOUNT_DELETE);
        // 2. 외부 호출 (트랜잭션 X)
        // todo: 외부 호출이 빠르고, 트랜잭션 대기 시간이 짧기 때문에 큰 문제가 되지 않는다고 판단
        try {
            keycloakService.deleteUser(userId);
        } catch (Exception e) {
            log.error("[deleteUser] Keycloak 사용자 삭제 실패: {}", e.getMessage());
            // 트랜잭션 롤백
            throw new RuntimeException("Failed to delete user in Keycloak", e);
        }
    }
}
