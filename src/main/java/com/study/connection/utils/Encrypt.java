package com.study.connection.utils;
import com.study.connection.error.CustomRuntimeException;
import com.study.connection.error.ErrorCode;
import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

import static com.study.connection.utils.CheckValid.checking;

/**
 * 단방향 암호화 기능 제공.
 */
public class Encrypt {
    /**
     * 단방향 암호화된 비밀번호 반환.
     * @param password 비밀번호
     * @return 단방향 암호화된 비밀번호
     */
    public String Encryption(@NotNull String password){
        String encrypted;
        try{
            byte[] plainText;
            byte[] hashValue;
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            plainText = password.getBytes();
            hashValue = messageDigest.digest(plainText);
            encrypted =  new String(Base64.getEncoder().encode(hashValue));
        } catch (NoSuchAlgorithmException e){
            throw new CustomRuntimeException(ErrorCode.ON_ENCRYPT_NO_ALGORITHM);
        }
        return encrypted;
    }

    /**
     * 사용자가 입력한 비밀번호가 유효한지 판단한 후 , db 에 저장되어져 있던 비밀번호와 동일한지 여부 확인
     * @param password 암호화가 필요한 비밀번호
     * @param original 암호화된 비밀번호
     * @return 두 매개변수 비교 후 동일여부 반환.
     */
    public Boolean checkEncrypt (String password , String original){
        boolean same = false;
        if(checking.checkString(password)){
            String encrypted = this.Encryption(password);
            if(Objects.equals(original, encrypted)){
                same = true;
            }
        } else{
            throw new CustomRuntimeException(ErrorCode.INVALID_STRING_ARGUMENT);
        }
        return same;
    }
}
