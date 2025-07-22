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

// Slf4j -logger

@Slf4j
@Component
@RequiredArgsConstructor  // provider 자동생성
public class JwtFilter extends OncePerRequestFilter {

	//의존성 주입 
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // 요청 헤더에서 토큰 조회
        String token = resolveToken(request);

        // 토큰이 존재하고 유효성 검증 통과 시
        if (StringUtils.hasText(token) && jwtProvider.validateToken(token)) {
            // 토큰에서 loginId 추출
            String loginId = jwtProvider.getLoginID(token);

            //  UsernamePasswordAuthenticationToken 생성 비밀번호는 비우고(보안), 권한도 따로 구분안해놨음 ㅎㅎ
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(loginId, null, null);

            // 인증 정보에 요청 세부정보 설정
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // SecurityContext에 인증 객체 등록
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 다음 필터로 요청 넘기기
        filterChain.doFilter(request, response);
    }

    // Request Header에서 토큰 조회 및 Bearer 문자열 제거 후 반환하는 메소드
    private String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        // Token 정보 존재 여부 및 Bearer 토큰인지 확인 -> Bearer부분 잘라내기
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }

        return null;
    }
}
