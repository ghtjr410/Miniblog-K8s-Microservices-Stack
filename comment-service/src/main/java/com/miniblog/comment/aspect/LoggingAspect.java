package com.miniblog.comment.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @AfterReturning(pointcut = "execution(* com.miniblog.comment.service.CommentService.*(..))", returning = "result")
    public void logSuccess(JoinPoint joinPoint, Object result) {
        log.info("Successfully executed: {} with result = {}", joinPoint.getSignature(), result);
    }
}
