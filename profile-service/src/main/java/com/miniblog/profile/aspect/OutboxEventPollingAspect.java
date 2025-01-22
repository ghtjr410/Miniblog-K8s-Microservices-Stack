package com.miniblog.profile.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Aspect
@Component
public class OutboxEventPollingAspect {
    
    // OutboxEventPolling만 타게팅
    @Pointcut("execution(* com.miniblog.profile.service.outbox.OutboxEventPolling.pollPendingEvents(..))")
    public void pollPendingEventsMethod() {
    }

    // pollPendingEvents 메서드 실행 후 반환값을 처리하는 Advice
    @AfterReturning(pointcut = "pollPendingEventsMethod()", returning = "result")
    public void logIfUnprocessedEventsExist(JoinPoint joinPoint, Object result) {
        if (result instanceof List<?>) {
            List<?> unprocessedEvents = (List<?>) result;
            if (!unprocessedEvents.isEmpty()) {
                log.info("pollPendingEvents completed Processed events count: {}", unprocessedEvents.size());
            }
        }
    }
}
