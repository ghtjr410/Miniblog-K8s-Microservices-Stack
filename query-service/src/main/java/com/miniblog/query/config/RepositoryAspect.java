package com.miniblog.query.config;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class RepositoryAspect {
    private final ObservationRegistry observationRegistry;

    @Around("execution(* org.springframework.data.mongodb.repository.MongoRepository+.*(..))")
    public Object observeMongoRepositoryMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String methodName = proceedingJoinPoint.getSignature().getName();
        Observation observation = Observation.start("mongo.repository." + methodName, observationRegistry);

        try {
            return observation.observe(() -> {
                try {
                    return proceedingJoinPoint.proceed();
                } catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
                }
            });
        } finally {
            observation.stop();
        }
    }
}