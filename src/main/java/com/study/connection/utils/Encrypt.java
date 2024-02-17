package com.study.connection.utils;
import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

import static com.study.connection.utils.CheckValid.checking;

/**
 * 단방향 암호화를 위한 기능 제공.
 */
public class Encrypt {
    /**
     * 비밀번호를 넣으면 단방향 암호화된 비밀번호가 나옴.
     */
    public String Encryption(@NotNull String password) throws NoSuchAlgorithmException {
        byte[] plainText;
        byte[] hashValue;
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        plainText = password.getBytes();
        hashValue = messageDigest.digest(plainText);
        return new String(Base64.getEncoder().encode(hashValue));
    }

    /**
     * 사용자가 입력한 비밀번호와 db 에 있는 비밀번호가 동일한지 여부 반환.
     * @param password
     * @param original
     * @return
     * @throws Exception
     */
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
