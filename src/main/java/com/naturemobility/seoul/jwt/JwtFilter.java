package com.naturemobility.seoul.jwt;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.BaseResponse;
import com.naturemobility.seoul.config.BaseResponseStatus;
import com.naturemobility.seoul.service.users.UsersService;
import com.naturemobility.seoul.utils.CookieUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private JwtService jwtService;
    private CookieUtil cookieUtil;

    @Override
    public void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        String jwt = resolveToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();
        String refreshJwt=null;

        try {
            if (StringUtils.hasText(jwt) && jwtService.validateToken(jwt)) {
                Authentication authentication = jwtService.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.debug("권한 {}", authentication.getAuthorities().toString());
                logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);

            } else {
                logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
                validateJwt(httpServletRequest, servletResponse, requestURI, refreshJwt);
            }
        }catch (ExpiredJwtException e){
            validateJwt(httpServletRequest, servletResponse, requestURI, refreshJwt);
        }

        filterChain.doFilter(httpServletRequest, servletResponse);

    }

    private void validateJwt(HttpServletRequest httpServletRequest, HttpServletResponse servletResponse, String requestURI, String refreshJwt) {
        try {
            Cookie refreshToken = cookieUtil.getCookie(httpServletRequest, JwtService.REFRESH_TOKEN);
            if (refreshToken != null) {
                refreshJwt = refreshToken.getValue();
            }
            if (refreshJwt != null && jwtService.isTokenExist(refreshJwt)) {
                Authentication authentication = jwtService.getAuthenticationFromRefreshToken(refreshJwt);
                String newJwt = jwtService.createJwt(authentication);
                jwtService.setTokens(servletResponse, authentication, newJwt, refreshJwt);
                logger.debug("권한 {}", authentication.getAuthorities().toString());
                logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
            } else {
                logger.debug("유효한 Refresh 토큰이 없습니다, uri: {}", requestURI);
                validateUrl(servletResponse, requestURI);
            }
        }catch (ExpiredJwtException e){
            logger.debug("유효한 Refresh 토큰이 없습니다, uri: {}", requestURI);
            validateUrl(servletResponse, requestURI);
        }
    }

    private void validateUrl(HttpServletResponse servletResponse, String requestURI) {
        logger.debug("url 확인");
        String[] needLoginUrl = {"/users/mypage","/users/check_password","/reservation","/review","/visited"};
        for (String url : needLoginUrl){
            if (requestURI.trim().startsWith(url)) {
                BaseResponse baseResponse = new BaseResponse(BaseResponseStatus.NEED_LOGIN);
//                try {
//                    logger.debug("need login 에러 발생");
//                    baseResponse.writeError(servletResponse, HttpStatus.INTERNAL_SERVER_ERROR.value());
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
            }
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}