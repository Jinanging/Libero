package com.jinanging.spring.libero.libero.user.address.aescrypt.key;

import java.security.SecureRandom;
import java.util.Base64;

public class SecretKey {

	public static void main(String[] args) {
        String key = generateEncryptionKey();
        System.out.println("Generated Base64-encoded AES-256 encryption key: " + key);
    }

    public static String generateEncryptionKey() {
        byte[] key = new byte[32];
        new SecureRandom().nextBytes(key);
		return Base64.getEncoder().encodeToString(key);
    }


}
