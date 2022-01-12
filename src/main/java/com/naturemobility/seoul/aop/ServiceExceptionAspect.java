package com.naturemobility.seoul.aop;

import com.naturemobility.seoul.config.BaseException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ServiceExceptionAspect {
    //TODO:utils 의 함수도 포함
    @Around("execution(* com.naturemobility.seoul.service.*.*(..)) || execution(* com.naturemobility.seoul.utils.*.*(..))")
    public Object serviceExceptionHandler(ProceedingJoinPoint proceedingJoinPoint) throws BaseException {
        try{
            return proceedingJoinPoint.proceed();
        }catch (BaseException e){
            throw new BaseException(e.getStatus());
        }catch (Throwable e){
            throw new RuntimeException(e.getMessage());
        }
    }

}
