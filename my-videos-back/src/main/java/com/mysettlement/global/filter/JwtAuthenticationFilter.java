package com.mysettlement.global.filter;

import com.mysettlement.global.jwt.CookieJwtResolver;
import com.mysettlement.global.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.mysettlement.global.jwt.JwtConstants.ACCESS_TOKEN;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CookieJwtResolver cookieJwtResolver;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = cookieJwtResolver.resolveToken(request, ACCESS_TOKEN);

        if (StringUtils.hasText(token)) {
            try {
                if (jwtUtil.isValidToken(token)) {
                    String username = jwtUtil.getUsername(token);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
                    log.debug("JWT authentication successful for path: {}", request.getRequestURI());
                } else {
                    log.warn("Invalid JWT token");
                }
            } catch (Exception e) {
                log.error("Failed to authenticate JWT token", e);
            }
        }

        // 토큰이 없거나 유효하지 않아도 다음 필터로 넘김
        filterChain.doFilter(request, response);
    }
}
