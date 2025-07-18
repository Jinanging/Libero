package com.jinanging.spring.libero.libero.bcrypt;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class BCryptEncryptor implements Encryptor {

    // 원본 비밀번호를 BCrypt 해시로 암호화하는 메서드
    @Override
    public String encrypt(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // 원본 비밀번호와 해시된 비밀번호가 일치하는지 확인하는 메서드 같으면 true 아니면 false
    @Override
    public boolean isMatch(String password, String hashedPassword) {
        try {
            return BCrypt.checkpw(password, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }
}
