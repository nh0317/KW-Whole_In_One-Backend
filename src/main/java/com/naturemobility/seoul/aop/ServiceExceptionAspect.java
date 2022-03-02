package com.naturemobility.seoul.aop;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.utils.ReadReqBody;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

@Component
@Aspect
@Log4j2
public class ServiceExceptionAspect {
    @Around("execution(* com.naturemobility.seoul.service.*.*(..)) || execution(* com.naturemobility.seoul.utils.*.*(..))  || execution(* com.naturemobility.seoul.mapper.*.*(..))")
    public Object serviceExceptionHandler(ProceedingJoinPoint proceedingJoinPoint) throws BaseException {
        try{
            return proceedingJoinPoint.proceed();
        }catch (BaseException e){
//            log.info(e.getStatus()+" "+e.getMessage());
            printErrorLog(e);
            throw new BaseException(e.getStatus());
        }catch (Throwable e){
//            log.info(e.getMessage());
            printErrorLog(e);
            throw new RuntimeException(e.getMessage());
        }
    }
    private void printErrorLog(Throwable exception) {
        String errorStack = "";
        Iterator<StackTraceElement> iterator = Arrays.stream(exception.getStackTrace()).iterator();
        while (iterator.hasNext())
            errorStack += iterator.next() + "\n";
        log.error(errorStack);
    }

}
