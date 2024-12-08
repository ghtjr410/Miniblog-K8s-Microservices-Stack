package com.miniblog.profile.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ProfileServiceAspect {

    // ProfileService만 타게팅
    @Pointcut("execution(* com.miniblog.profile.service.profile.ProfileService.*(..))")
    public void profileServiceMethods() {
    }

    @AfterReturning(pointcut = "profileServiceMethods()", returning = "result")
    public void logProfileService(JoinPoint joinPoint, Object result) {
        log.info("Successfully executed: {} with result = {}", joinPoint.getSignature(), result);
    }

    @Around("profileServiceMethods()")
    public Object logServiceExceptions(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (DataAccessException ex) {
            // 데이터 접근 예외는 그대로 던짐 (글로벌 예외 처리기에서 처리)
            throw ex;
        } catch (RuntimeException ex) {
            // 이미 RuntimeException으로 래핑된 경우 그대로 던짐
            throw ex;
        } catch (Exception ex) {
            // 예측하지 못한 예외를 로깅하고 RuntimeException으로 래핑하여 던짐 (글로벌 예외 처리기에서 처리)
            log.error("Unexpected exception in {}.{}() with cause = {}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    ex.getCause() != null ? ex.getCause() : "NULL",
                    ex);
            throw new RuntimeException("Unexpected error occurred in service layer", ex);
        }
    }
}
