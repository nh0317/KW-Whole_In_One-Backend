package com.naturemobility.seoul.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naturemobility.seoul.config.BaseResponse;
import com.naturemobility.seoul.config.BaseResponseStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import static com.naturemobility.seoul.config.BaseResponseStatus.NEED_LOGIN;
import static com.naturemobility.seoul.config.BaseResponseStatus.UNATHORIZED;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // 유효한 자격증명을 제공하지 않고 접근하려 할때 401
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        BaseResponse<String> baseResponse = new BaseResponse<>(NEED_LOGIN);
        baseResponse.writeError(response);

    }
}