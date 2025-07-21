package com.jinanging.spring.libero.libero.user.service;

import org.springframework.stereotype.Service;

import com.jinanging.spring.libero.libero.bcrypt.BCryptEncryptor;
import com.jinanging.spring.libero.libero.user.address.aescrypt.AesCryptor;
import com.jinanging.spring.libero.libero.user.domain.User;
import com.jinanging.spring.libero.libero.user.repository.UserRepository;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final BCryptEncryptor bcryptEncryptor;
    private final AesCryptor aesCryptor;

    public UserService(UserRepository userRepository, BCryptEncryptor bcryptEncryptor, AesCryptor aesCryptor) {
        this.userRepository = userRepository;
        this.bcryptEncryptor = bcryptEncryptor;
        this.aesCryptor = aesCryptor;
    }
    
    public Boolean duplicatedId(String loginId) {
    	
    	int count = userRepository.countByLoginId(loginId);
    	
    	if(count == 0) {
    		return false;
    	}
    	else {
    		return true;
    	}
    	
    	
    }


    public Boolean joinUser(
            String loginId,
            String password,
            String name,
            String address,
            String addressDetail,
            String addressNumber
    ) throws Exception {
    	// 비크립트 해싱 사용
    	String hashedPassword = bcryptEncryptor.encrypt(password);
    	// 주소도 aes 암호화 
			String aesAddress = aesCryptor.encrypt(address);
			String aesAddressDetail = aesCryptor.encrypt(addressDetail);
	    	String aesAddressNumber = aesCryptor.encrypt(addressNumber);
	
    			
        User user = User.builder()
        			.loginId(loginId)
        			//비크립트로 암호화된 비밀번호 저장
        			.password(hashedPassword)
        			.name(name)
        			.address(aesAddress)
        			.addressDetail(aesAddressDetail)
        			.addressNumber(aesAddressNumber)
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

