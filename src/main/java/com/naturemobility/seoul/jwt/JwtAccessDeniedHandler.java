package com.naturemobility.seoul.jwt;

import com.naturemobility.seoul.config.BaseResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.naturemobility.seoul.config.BaseResponseStatus.FORBIDDEN;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        //필요한 권한이 없이 접근하려 할때 403
//        response.sendError(HttpServletResponse.SC_FORBIDDEN);
        BaseResponse<String> baseResponse = new BaseResponse<>(FORBIDDEN);
        baseResponse.writeError(response);
    }
}