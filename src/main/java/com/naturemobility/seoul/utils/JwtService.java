package com.naturemobility.seoul.utils;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.secret.Secret;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static com.naturemobility.seoul.config.BaseResponseStatus.EMPTY_JWT;
import static com.naturemobility.seoul.config.BaseResponseStatus.INVALID_JWT;

@Service
public class JwtService {
    /**
     * JWT 생성
     * @param userIdx
     * @return String
     */
    public String createJwt(Long userIdx) {
        Date now = new Date();
        return Jwts.builder()
                .claim("userIdx", userIdx)
                .setIssuedAt(now)
                .signWith(Secret.JWT_SECRET_KEY)
                .compact();
    }

    /**
     * JWT 생성
     * @param userIdx
     * @return String
     */
    public String createPartnerJwt(Long userIdx) {
        Date now = new Date();
        return Jwts.builder()
                .claim("partnerIdx", userIdx)
                .setIssuedAt(now)
                .signWith(Secret.JWT_SECRET_KEY)
                .compact();
    }

    /**
     * Header에서 X-ACCESS-TOKEN 으로 JWT 추출
     * @return String
     */
    public String getJwt() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("X-ACCESS-TOKEN");
    }

    /**
     * JWT에서 userId 추출
     * @return int
     * @throws BaseException
     */
    public Long getUserIdx() throws BaseException {
        // 1. JWT 추출
        String accessToken = getJwt();
        if (accessToken == null || accessToken.length() == 0) {
            throw new BaseException(EMPTY_JWT);
        }

        // 2. JWT parsing
        Jws<Claims> claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(Secret.JWT_SECRET_KEY)
                    .parseClaimsJws(accessToken);
        } catch (Exception ignored) {
            throw new BaseException(INVALID_JWT);
        }

        // 3. userId 추출
        return claims.getBody().get("userIdx", Integer.class).longValue();
    }
    /**
     * JWT에서 userId 추출
     * @return int
     * @throws BaseException
     */
    public Long getPartnerIdx() throws BaseException {
        // 1. JWT 추출
        String accessToken = getJwt();
        if (accessToken == null || accessToken.length() == 0) {
            throw new BaseException(EMPTY_JWT);
        }

        // 2. JWT parsing
        Jws<Claims> claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(Secret.JWT_SECRET_KEY)
                    .parseClaimsJws(accessToken);
        } catch (Exception ignored) {
            throw new BaseException(INVALID_JWT);
        }

        // 3. userId 추출
        return claims.getBody().get("partnerIdx", Integer.class).longValue();
    }
}
