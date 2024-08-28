package com.example.SpringSecurityApplication.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("within(com.example.SpringSecurityApplication.controllers..*)")
    public void controllerPointcut() {}

    @Around("controllerPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.info("Выполнение метода {} с аргументами {}", methodName, args);

        Object result = joinPoint.proceed(args);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        log.info("Метод {} выполнился за {} мс с результатом {}", methodName, duration, result);

        return result;
    }


    @AfterThrowing(pointcut = "controllerPointcut()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Exception e) {
        log.error("Произошла ошибка при вызове метода: {}", joinPoint.getSignature().toLongString());
        log.error("Ошибка: {}", e.getMessage());
    }
}
