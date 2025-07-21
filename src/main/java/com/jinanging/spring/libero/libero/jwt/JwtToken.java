package com.jinanging.spring.libero.libero.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;

@Builder
@Data
@AllArgsConstructor
public class JwtToken {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
