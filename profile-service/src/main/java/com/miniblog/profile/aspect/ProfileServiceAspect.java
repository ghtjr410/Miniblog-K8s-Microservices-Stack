package com.miniblog.profile.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
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
        } catch (RuntimeException ex) {
            log.error("Exception in {}.{}() with cause = {}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    ex.getCause() != null ? ex.getCause() : "NULL",
                    ex);
            throw ex;
        }
    }
}
