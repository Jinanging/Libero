package com.jinanging.spring.libero.libero.jwt;

public class JwtConstants {

    // Expiration Time - > 만료시간 
    public static final long MINUTE = 1000 * 60;
    public static final long HOUR = 60 * MINUTE;
    public static final long DAY = 24 * HOUR;
    public static final long MONTH = 30 * DAY;

    //Access Token 만료 시간 : 10분 =>테스트를 위해 짧게 설정했지만 실제로는 1~2시간 정도
    public static final long AT_EXP_TIME =  60 * MINUTE;

    //refresh Token 만료 시간 : 3시 => 실제로는 일주일~이주일 정도
    public static final long RT_EXP_TIME =  180 * MINUTE;

    // Secret key => 유출되지 않는 것이 중요하다. 실제로는 아주 복잡하고 비밀스러운 키 사용
		// AccessToken을 암호화하기 위한 키
    public static final String JWT_SECRET_AT = "firesea test server 1";
    //RefreshToken을 암호화하기 위한 키
    public static final String JWT_SECRET_RT = "firesea test server 2";

    // Header
    public static final String AT_HEADER = "access_token";
    public static final String RT_HEADER = "refresh_token";
    public static final String TOKEN_HEADER_PREFIX = "Bearer ";
}
