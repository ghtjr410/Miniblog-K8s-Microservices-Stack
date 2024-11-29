package com.miniblog.post.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
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
