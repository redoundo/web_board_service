package com.study.connection.utils;
import lombok.NonNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

import static com.study.connection.utils.CheckValid.checking;


public class Encrypt {
    public String Encryption(@NonNull String password) throws NoSuchAlgorithmException {
        byte[] plainText;
        byte[] hashValue;
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        plainText = password.getBytes();
        hashValue = messageDigest.digest(plainText);
        return new String(Base64.getEncoder().encode(hashValue));
    }
    public Boolean checkEncrypt (String password , String original) throws Exception {
        boolean same = false;
        try{
            if(checking.checkString(password)){
                String encrypted = new Encrypt().Encryption(password);
                if(Objects.equals(original, encrypted)){
                    same = true;
                }
            } else{
                throw new Exception("id 혹은 password 가 유효하지 않습니다.");
            }
        }catch (Exception e){
            throw new Exception(e);
        }
        return same;
    }
}
