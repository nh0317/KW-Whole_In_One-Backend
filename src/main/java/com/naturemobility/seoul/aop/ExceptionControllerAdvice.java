package com.naturemobility.seoul.aop;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.BaseResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.Iterator;

import static com.naturemobility.seoul.config.BaseResponseStatus.RESPONSE_ERROR;

@RestControllerAdvice
@Log4j2
public class ExceptionControllerAdvice {
    @ExceptionHandler(BaseException.class)
    public BaseResponse<Object> baseExceptionHandler(BaseException exception, HttpServletRequest req){
        if(exception.getStatus().equals(RESPONSE_ERROR)){
            log.error("URI : {} Query : {}", req.getRequestURI(), req.getQueryString());
            //TODO:request body 출력
            //TODO:slack 연동
            exception.printStackTrace();
        }else{
            log.warn("Message : {}", exception.getStatus().getMessage());
            log.warn("URI : {} Query : {}",req.getRequestURI(), req.getQueryString());
        }
        return new BaseResponse<>(exception.getStatus());
    }
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<Object> runtimeExceptionHandler(RuntimeException exception, HttpServletRequest req){
        log.error("URI : {} Query : {}", req.getRequestURI(), req.getQueryString());
        Iterator<StackTraceElement> iterator = Arrays.stream(exception.getStackTrace()).iterator();
        exception.printStackTrace();
        return new BaseResponse<>(RESPONSE_ERROR);
    }
}
