package com.jinanging.spring.libero.libero.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jinanging.spring.libero.libero.api.ApiResponse;
import com.jinanging.spring.libero.libero.api.ResponseCode;
import com.jinanging.spring.libero.libero.bcrypt.BCryptEncryptor;
import com.jinanging.spring.libero.libero.jwt.JwtProvider;
import com.jinanging.spring.libero.libero.user.domain.User;
import com.jinanging.spring.libero.libero.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptEncryptor bcryptEncryptor;
    private final JwtProvider jwtProvider;

    public ApiResponse<String> login(String loginId, String password) {
    	Optional<User> optionalUser = userRepository.findByLoginId(loginId);
    	if (optionalUser.isEmpty()) {
    	    return ApiResponse.fail(ResponseCode.LOGIN_FAILED);
    	}
    	User user = optionalUser.get();


        if (!bcryptEncryptor.isMatch(password, user.getPassword())) {
        	// api response 새로 만들었당 ㅎㅎ
            return ApiResponse.fail(ResponseCode.LOGIN_FAILED);
        }
        else {
        // 로그인 성공 -> JWT 토큰 발급
        String token = jwtProvider.generateToken(user);
        return ApiResponse.success(token);
        }
    }
}
