package com.study.connection;
import lombok.NonNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Encrypt {
    public String Encryption(@NonNull String password) throws NoSuchAlgorithmException {
        byte[] plainText;
        byte[] hashValue;
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        plainText = password.getBytes();
        hashValue = messageDigest.digest(plainText);
        return new String(Base64.getEncoder().encode(hashValue));
    }
}
