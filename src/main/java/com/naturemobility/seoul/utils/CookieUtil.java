package com.naturemobility.seoul.utils;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
public class CookieUtil {
    public Cookie createCookie(String name, String value, int validity){
        Cookie token = new Cookie(name, value);
        token.setHttpOnly(true);
        token.setMaxAge(validity);
        token.setPath("/");
        return token;
    }
    public Cookie getCookie(HttpServletRequest req, String name){
        final Cookie[] cookies = req.getCookies();
        if(cookies==null) return null;
        for(Cookie cookie:cookies){
            if(cookie.getName().equals(name))
                return cookie;
        }
        return null;
    }
}
