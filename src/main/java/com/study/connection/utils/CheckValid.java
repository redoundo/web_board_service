package com.study.connection.utils;

import java.lang.reflect.Field;

/**
 *
 */
public class CheckValid {
    public static CheckValid checking = new CheckValid();
    public boolean checkString(String needCheck){
        return needCheck != null && !needCheck.isEmpty() && !needCheck.equals("null");
    }
    public boolean checkClassMembers(Object object) throws IllegalAccessException {
        boolean valid = true;
        if(object != null){
            for (Field field: object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if(field.get(object) == null){
                    valid = false;
                }
            }
        }
        return valid;
    }
}
