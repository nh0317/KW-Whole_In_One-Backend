package com.naturemobility.seoul.jwt;

import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.config.BaseResponseStatus;
import com.naturemobility.seoul.config.SecretPropertyConfig;
import com.naturemobility.seoul.redis.RedisService;
import com.naturemobility.seoul.utils.CookieUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtService implements InitializingBean {
    @Autowired
    SecretPropertyConfig secretPropertyConfig;

    @Autowired
    CookieUtil cookieUtil;

    @Autowired
    RedisService redisService;

    private final Logger logger = LoggerFactory.getLogger(JwtService.class);

    private static final String AUTHORITIES_KEY = "auth";

    private Key key;

    public static final String REFRESH_TOKEN="refresh_toke";

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secretPropertyConfig.getUserInfoPasswordKey());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createJwt(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + Long.parseLong(secretPropertyConfig.getTokenValidityInSeconds()) * 1000);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    public String createRefreshJwt(Authentication authentication) {
        long now = (new Date()).getTime();
        Date validity = new Date(now + Long.parseLong(secretPropertyConfig.getRefreshTokenValidityInSeconds()) * 1000);

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
//                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        try{
            Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Collection<? extends GrantedAuthority> authorities =
                    Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

            User principal = new User(claims.getSubject(), "", authorities);

            return new UsernamePasswordAuthenticationToken(principal, token, authorities);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    public Authentication getAuthenticationFromRefreshToken(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        String email = redisService.getValuse(token);

        User principal = new User(email, "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.info("만료된 JWT 토큰입니다.");
            throw e;
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public void setTokens(HttpServletResponse res, Authentication authentication, String jwt, String refreshJwt) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Cookie cookie=cookieUtil.createCookie(JwtService.REFRESH_TOKEN,
                refreshJwt, Integer.parseInt(secretPropertyConfig.getRefreshTokenValidityInSeconds()));
        res.addCookie(cookie);
        res.setHeader(JwtFilter.AUTHORIZATION_HEADER,"Bearer " + jwt);

        redisService.setValues(refreshJwt,authentication.getName());
    }
    public void expireTokens(HttpServletResponse res, Cookie refreshToken){
        cookieUtil.expireCookie(res,refreshToken.getName());
        res.setHeader(JwtFilter.AUTHORIZATION_HEADER,null);
    }
    public Boolean isTokenExist(String token){
        return redisService.isExist(token);
    }
}
