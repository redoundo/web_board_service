package com.study.connection.controller;

import com.study.connection.utils.Encrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import static com.study.connection.utils.CheckValid.checking;

@Controller
public class CheckPasswordController {
    /**
     * 삭제나 변경을 진행하기 전에 ajax 통신으로 입력한 비밀번호의 유효성을 먼저 확인 하는 단계
     * @param original db 에 저장된 비밀번호
     * @param password 사용자가 입력한 비밀번호
     * @return original 과 password 가 동일한지 여부
     * @throws Exception
     */
    @RequestMapping(value = {"/view/checkPassword" , "/view/modify/checkPassword"} , method = RequestMethod.POST)
    public boolean checkPassword(@RequestParam("original") String original , @RequestParam("password") String password)
            throws Exception {
        boolean same = false;
        if(checking.checkString(original) && checking.checkString(password)){
            same = new Encrypt().checkEncrypt(password , original);
        }
        return same;
    }
}
