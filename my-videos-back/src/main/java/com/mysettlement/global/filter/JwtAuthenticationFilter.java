package com.mysettlement.global.filter;

import com.mysettlement.global.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        // 인증이 필요없는 경로는 바로 통과
        if (path.startsWith("/v1/users/login") || path.startsWith("/v1/users/signup")) {
            log.info(path);
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = jwtUtil.resolveAccessToken(request);

            if (StringUtils.hasText(token) && jwtUtil.isValidToken(token)) {
                Authentication authentication = jwtUtil.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("JWT authentication successful for path: {}", path);
            } else {
                log.warn("Invalid or missing JWT token for path: {}", path);
                // 인증이 필요한 경로에서 토큰이 없으면 401 응답
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\":\"Authentication required\"}");
                return;
            }
        } catch (Exception e) {
            log.error("JWT authentication failed for path: {}", path, e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Authentication failed\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
