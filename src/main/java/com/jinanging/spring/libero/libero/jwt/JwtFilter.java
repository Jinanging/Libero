package com.jinanging.spring.libero.libero.jwt;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // 1. 토큰 추출 (헤더 or 쿠키)
        String token = resolveToken(request);

        // 2. 토큰이 존재하고 유효하다면 인증 처리
        if (StringUtils.hasText(token) && jwtProvider.validateToken(token)) {
            String loginId = jwtProvider.getLoginID(token);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(loginId, null, null);

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 3. 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

    // 헤더 Authorization 또는 쿠키에서 토큰을 꺼내는 메서드
    private String resolveToken(HttpServletRequest request) {
        // 1) Authorization 헤더에서 토큰 추출 (Bearer 토큰)
        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);  // "Bearer " 다음 부분만 리턴
        }

        // 2) 헤더에 없으면 쿠키에서 "Authorization" 이름으로 토큰 찾기
        if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                if ("Authorization".equals(cookie.getName()) && StringUtils.hasText(cookie.getValue())) {
                    return cookie.getValue();
                }
            }
        }

        // 토큰이 없으면 null 리턴
        return null;
    }
}
