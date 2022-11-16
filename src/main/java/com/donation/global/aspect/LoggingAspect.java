package com.donation.global.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* com.donation.presentation.*Controller.*(..))")
    private void loggingTarget() {

    }

    @Before("loggingTarget()")
    public void logRequest(final JoinPoint joinPoint) {
        CodeSignature signature = (CodeSignature) joinPoint.getSignature();

        log.info("[Request] Controller: {} / Method: {} / Arguments: {}",
                joinPoint.getTarget().getClass().getSimpleName(),
                signature.getName(),
                toString(signature.getParameterNames(), joinPoint.getArgs())
        );
    }

    private String toString(final String[] parameterNames, final Object[] args) {
        return IntStream.range(0, parameterNames.length)
                .mapToObj(i -> parameterNames[i] + "=" + args[i])
                .collect(Collectors.joining(","));
    }
}
