package com.jinanging.spring.libero.libero.user.address.aescrypt.key;

import java.security.SecureRandom;
import java.util.Base64;

public class IVKey {
	public static void main(String[] args) {
        String ivKey = generateIVKey();
        System.out.println("Generated Base64-encoded IV: " + ivKey);
    }

    public static String generateIVKey() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);

        return Base64.getEncoder().encodeToString(iv);
    }

}
