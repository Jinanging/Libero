package com.jinanging.spring.libero.libero.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity 
@RequiredArgsConstructor
public class SecurityConfig {

    // JWT 필터 의존성 주입
    private final JwtFilter jwtFilter;

    // cors를 활성화 csrf를 비활성화(api서버이기 떄문에)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                
                // JWT 필터를 UsernamePasswordAuthenticationFilter 전에 등록해서 인증 처리함
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                // 일단 모든 요청 허용 (필요시 권한 설정 추가 가능)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        return http.build();
    }

    // http://localhost:8080에서 오는 요청은 OK.
    // 어떤 HTTP 메소드든 OK.
    // 인증정보(쿠키 등)도 허용.
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // 인증정보 주고받도록 허용(쿠키)
        config.setAllowCredentials(true);
        //
        config.setAllowedOrigins(List.of("http://localhost:8080"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        // 어떤 헤더든 사용가능
        config.setAllowedHeaders(List.of("*"));
        // front에서 응답 헤더를 볼수 있게
        config.setExposedHeaders(List.of("*")); 
        
        // 내가 설정한 모든 경로 cors적용
        // 해커들이 유저인척하고 메소드 보내는걸 막으려고 하는 설정 -> origin(유저)만 보낼수 있음!
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return source;
    }
}
