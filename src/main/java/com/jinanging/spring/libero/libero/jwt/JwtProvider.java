package com.jinanging.spring.libero.libero.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jinanging.spring.libero.libero.user.domain.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
@Component
@RequiredArgsConstructor
public class JwtProvider {
	
	// 보안폴더에 설정해논 친구들
    @Value("${jwt.secret}")
    private String secretKey;
    private Key key;
    @Value("${jwt.expiration-time}")
    private long expirationTime;

    // 비밀키 디코딩하고 jwt 서명용 키 객체 셍성
    // 포스트 컨스트럭트 어노테이션으로 다시 초기화 되니까 토큰을 한번만 만들어두 된다 아니면 매번 토큰생성해서 서버부
    @PostConstruct
    protected void init() {
        byte[] secretKeyBytes = Decoders.BASE64.decode(secretKey);
        key = Keys.hmacShaKeyFor(secretKeyBytes);
    }

    /**
     * JWT 생성
     * 사용자 정보 기반으로 jwt토큰 생성
     * 토큰 발행시간과 만료 시간도 설정하기 
     *  hs256(sha256) 으로 서명후 jwt 토큰 문자열 생성!
     */
    public String generateToken(User user) {
        Claims claims = getClaims(user);

        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    
    // 유효성 검사
    // 서명이 올바른지? 유효시간이 안지났는지 ?
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return claims.getBody().getExpiration().after(new Date());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    // 토근에서 사용자 식별정보인 loginID를 가져온다!
    public String getLoginID(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * 토큰의 만료기한 반환
     * 리프레시 토큰 검증에 활용하려고
     */
    public Long getExpirationTime(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration().getTime();
    }

    /**
     * Claims 정보 생성
     * 중복검사를 진행하는 user의 로그인 아이디로 진행
     */
    private Claims getClaims(User user) {
        return Jwts.claims().setSubject(user.getLoginId());
    }
}
