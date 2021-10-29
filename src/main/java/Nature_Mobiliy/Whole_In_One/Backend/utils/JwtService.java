package Nature_Mobiliy.Whole_In_One.Backend.utils;

import Nature_Mobiliy.Whole_In_One.Backend.config.BaseException;
import Nature_Mobiliy.Whole_In_One.Backend.config.secret.Secret;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static Nature_Mobiliy.Whole_In_One.Backend.config.BaseResponseStatus.EMPTY_JWT;
import static Nature_Mobiliy.Whole_In_One.Backend.config.BaseResponseStatus.INVALID_JWT;

@Service
public class JwtService {
    /**
     * JWT 생성
     * @param userId
     * @return String
     */
    public String createJwt(int userId) {
        Date now = new Date();
        return Jwts.builder()
                .claim("userId", userId)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, Secret.JWT_SECRET_KEY)
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
    public int getUserId() throws BaseException {
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
        return claims.getBody().get("userId", Integer.class);
    }
}
