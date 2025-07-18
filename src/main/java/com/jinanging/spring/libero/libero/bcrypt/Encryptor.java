package com.jinanging.spring.libero.libero.bcrypt;

public interface Encryptor {
	
    String encrypt(String password);
    
    boolean isMatch(String password, String hashedPassword);
}

