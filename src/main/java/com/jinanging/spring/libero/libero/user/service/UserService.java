package com.jinanging.spring.libero.libero.user.service;

import java.util.Optional;

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
        return count != 0;
    }

    public Boolean joinUser(
            String loginId,
            String password,
            String name,
            String address,
            String addressDetail,
            String addressNumber
    ) throws Exception {
        String hashedPassword = bcryptEncryptor.encrypt(password);
        String aesAddress = aesCryptor.encrypt(address);
        String aesAddressDetail = aesCryptor.encrypt(addressDetail);
        String aesAddressNumber = aesCryptor.encrypt(addressNumber);

        User user = User.builder()
                .loginId(loginId)
                .password(hashedPassword)
                .name(name)
                .address(aesAddress)
                .addressDetail(aesAddressDetail)
                .addressNumber(aesAddressNumber)
                .profile("https://cdn.pixabay.com/photo/2018/04/24/11/32/book-3346785_1280.png")
                .build();

        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Optional<User> 받아서 없으면 null 리턴하도록 변경
    public User findByLoginId(String loginId) {
        Optional<User> optionalUser = userRepository.findByLoginId(loginId);
        return optionalUser.orElse(null);
    }
}
