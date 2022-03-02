package com.naturemobility.seoul.aop;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.BaseResponse;
import com.naturemobility.seoul.utils.ReadReqBody;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;

import static com.naturemobility.seoul.config.BaseResponseStatus.REQUEST_ERROR;
import static com.naturemobility.seoul.config.BaseResponseStatus.RESPONSE_ERROR;

@RestControllerAdvice
@Log4j2
public class ExceptionControllerAdvice {
    @ExceptionHandler(BaseException.class)
    public BaseResponse<Object> baseExceptionHandler(BaseException exception, HttpServletRequest req){
        if(exception.getStatus().equals(RESPONSE_ERROR) || exception.getStatus().equals(REQUEST_ERROR)){
            log.info(exception.getStatus()+" "+exception.getMessage());
            printErrorLog(req, exception);
        }else{
            log.warn("Message : {}", exception.getStatus().getMessage());
            log.warn("URI : {} Query : {}",req.getRequestURI(), req.getQueryString());
        }
        return new BaseResponse<>(exception.getStatus());
    }
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<Object> runtimeExceptionHandler(RuntimeException exception, HttpServletRequest req){
        log.info(exception.getMessage());
        printErrorLog(req, exception);
        return new BaseResponse<>(RESPONSE_ERROR);
    }

    private void printErrorLog(HttpServletRequest req, Exception exception) {
        String errorStack = "";
        errorStack += "URI : " + req.getRequestURI() + " Query : " + req.getQueryString()+"\n";
        if (req.getContentType()!=null && !req.getContentType().startsWith("multipart/form-data")) {
            try {
                String body = ReadReqBody.getBody(req);
//                if (!body.contains("password"))
                errorStack += body+"\n";
            } catch (IOException e) {
                return;
            }
        }
        Iterator<StackTraceElement> iterator = Arrays.stream(exception.getStackTrace()).iterator();
        while (iterator.hasNext())
            errorStack += iterator.next() + "\n";
        log.error(errorStack);
    }
}
