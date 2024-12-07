package com.miniblog.post.aspect;

import com.miniblog.post.exception.NotFoundException;
import com.miniblog.post.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class PostServiceAspect {

    @Pointcut("execution(* com.miniblog.post.service.post.PostService.*(..))")
    public void postServiceMethods() {
    }

    @After("postServiceMethods()")
    public void logPostService(JoinPoint joinPoint) {
        log.info("Successfully executed: {}", joinPoint.getSignature());
    }

    @Around("postServiceMethods()")
    public Object logServiceExceptions(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (NotFoundException | UnauthorizedException | DataAccessException ex) {
            // 비즈니스 예외 및 데이터 접근 예외는 그대로 던짐 (글로벌 예외 처리기에서 처리)
            throw ex;
        } catch (Exception ex) {
            // 예측하지 못한 예외를 로깅하고 RuntimeException으로 래핑
            log.error("Unexpected exception in {}.{}() with cause = {}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    ex.getCause() != null ? ex.getCause() : "NULL",
                    ex);
            throw new RuntimeException("Unexpected error occurred in service layer", ex);
        }
    }
}
