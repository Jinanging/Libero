package com.jinanging.spring.libero.libero.user.address.aescrypt;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class AesCryptor {


    private final byte[] secretKeyBytes;
    private final byte[] ivBytes;

    public AesCryptor(String base64SecretKey, String base64IvKey) {
        this.secretKeyBytes = Base64.getDecoder().decode(base64SecretKey);
        this.ivBytes = Base64.getDecoder().decode(base64IvKey);
    }

    public String encrypt(String plaintext) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyBytes, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String encryptedText) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyBytes, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, "UTF-8");
    }
}
