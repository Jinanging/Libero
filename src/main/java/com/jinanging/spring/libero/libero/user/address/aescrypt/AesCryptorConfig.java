package com.jinanging.spring.libero.libero.user.address.aescrypt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AesCryptorConfig {

    @Value("${aes.secret-key}")
    private String secretKey;

    @Value("${aes.iv-key}")
    private String ivKey;

    @Bean
    public AesCryptor aesCryptor() {
        return new AesCryptor(secretKey, ivKey);
    }
}
