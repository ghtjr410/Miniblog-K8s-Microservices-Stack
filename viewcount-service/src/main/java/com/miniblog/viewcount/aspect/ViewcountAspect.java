package com.miniblog.viewcount.aspect;

import com.miniblog.viewcount.exception.NotFoundException;
import jakarta.persistence.OptimisticLockException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ViewcountAspect {

    @Pointcut("execution(* com.miniblog.viewcount.service.viewcount.ViewcountService.*(..))")
    public void viewcountServiceMethods() {
    }

    @After("viewcountServiceMethods()")
    public void logViewcountService(JoinPoint joinPoint) {
        log.info("Successfully executed: {}", joinPoint.getSignature());
    }

    @Around("viewcountServiceMethods()")
    public Object logServiceExceptions(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (NotFoundException | DataAccessException | OptimisticLockException ex) {
            // 비즈니스 예외 및 데이터 접근 예외, 낙관적 락 예외는 그대로 던짐 (글로벌 예외 처리기에서 처리)
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
