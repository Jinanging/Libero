package com.jinanging.spring.libero.libero.user.service;

import org.springframework.stereotype.Service;

import com.jinanging.spring.libero.libero.bcrypt.BCryptEncryptor;
import com.jinanging.spring.libero.libero.user.domain.User;
import com.jinanging.spring.libero.libero.user.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptEncryptor bcryptEncryptor;

    public UserService(UserRepository userRepository, BCryptEncryptor bcryptEncryptor) {
        this.userRepository = userRepository;
        this.bcryptEncryptor = bcryptEncryptor;
    }

    public Boolean joinUser(
            String loginId,
            String password,
            String name,
            String address,
            String addressDetail,
            String addressNumber
    ) {
    	// 비크립트 해싱 사용
    	String hashedPassword = bcryptEncryptor.encrypt(password);
        User user = User.builder()
        			.loginId(loginId)
        			//비크립트로 암호화된 비밀번호 저장
        			.password(hashedPassword)
        			.name(name)
        			.address(address)
        			.addressDetail(addressDetail)
        			.addressNumber(addressNumber)
        			.profile("https://cdn.pixabay.com/photo/2018/04/24/11/32/book-3346785_1280.png")
        			.build();
        
        // 회원가입이 널일떄.
        try {
        	
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }

   
            
    }
}

