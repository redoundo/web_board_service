package com.study.connection.utils;
/**
 *
 */
public class CheckValid {
    public static CheckValid checking = new CheckValid();
    public boolean checkString(String needCheck){
        return needCheck != null && !needCheck.isEmpty() && !needCheck.equals("null");
    }
}
