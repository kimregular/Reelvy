package com.reelvy.global.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CookieJwtResolver {

    public String resolveToken(HttpServletRequest request, JwtConstants jwtType) {
        Cookie cookie = jwtType.from(request);
        if(cookie != null) {
            return cookie.getValue();
        }
        log.info("Access token not found");
        return null;
    }
}
