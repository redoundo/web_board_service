package com.study.connection.utils;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 입력된 object 의 값이 null 이 아닐 때 특정 값을 반환한다.
 */
public class NotNullInClass {
    /**
     * 필드에 있는 내용의 이름이 what 과 같을 경우 해당 필드의 값을 리스트에 넣어 반환한다.
     * @param object 검증을 원하고 사용자지정 클래스로만 이뤄진 클래스.
     * @param what 반환하길 원하는 내용
     * @return object 안에 있는 사용자지정 클래스로 이뤄진 필드가 null 이 아닐 때 ,
     * 해당 필드에서 what 과 같은 이름의 값을 리스트에 넣어 반환.
     * ex) class a { String aa , Integral bb} 가 있고 class b { a Aa , a Bb } 같이 사용자 지정 클래스로 이뤄졌을 경우
     * object = class b , what = aa 라고 할때 , class a 의 aa 를 넣은 List<String> 을 반환.
     */
    public List<Object> getValuesByFieldName(@NotNull Object object ,@NotNull String what) {

        List<Object> objectList = new ArrayList<>();
        try{
            for(Field field : object.getClass().getDeclaredFields()){
                if(field != null){
                    field.setAccessible(true);

                    for(Field insideField : field.getClass().getDeclaredFields()){
                        insideField.setAccessible(true);
                        if(insideField.getName().equals(what)){
                            objectList.add(insideField.get(insideField));
                        }
                    }
                }
            }
        } catch (IllegalAccessException e){
            e.getStackTrace();
        }

        return  objectList;
    }

    /**
     * 사용자 지정 클래스가 아닌 경우 필드 내의 필드 내용을 가져올 게 없기 때문에 notNull 인 것만 제공.
     * @param object notNull 인 경우만 가져오고 싶은 클래스
     * @return notNull 인 필드들.
     */
    public List<Object> getNotNullValues(Object object) {

        List<Object> list = new ArrayList<>();
        try{
            if(object != null){
                for(Field field : object.getClass().getDeclaredFields()){
                    field.setAccessible(true);
                    if(field.get(object) != null){
                        list.add(field);
                    }
                }
            }
        }catch (IllegalAccessException e){
            e.getStackTrace();
        }
        return list;
    }

}
