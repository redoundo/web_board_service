package com.study.connection.utils;

import com.study.connection.dto.SearchConditionDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 유효성 여부 확인
 */
public class CheckValid {
    public static CheckValid checking = new CheckValid();
    public boolean checkString(String needCheck){
        return needCheck != null && !needCheck.isEmpty() && !needCheck.equals("null");
    }
    public boolean checkGrantedAuthority(@NotNull UserDetails details){
        GrantedAuthority auth = new SimpleGrantedAuthority("ROLE_ADMIN");
        return details.getAuthorities().contains(auth);
    }
    public SearchConditionDto notNullConditions(Map<String, Object> conditions){
        SearchConditionDto dto = SearchConditionDto.builder().build();

        if(conditions.get("start") != null && this.checkString(conditions.get("start").toString()))
            dto.setStart(Date.valueOf(conditions.get("start").toString()));
        if(conditions.get("end") != null && this.checkString(conditions.get("end").toString())) {
            String end = conditions.get("end").toString();
            List<String> endList = Arrays.stream(end.split("-")).toList();
            Integer endInt = Integer.parseInt(endList.get(2)) + 1;
            dto.setEnd(Date.valueOf(endList.get(0) + "-" + endList.get(1) + "-" + endInt));
        }
        if(conditions.get("keyword") != null && this.checkString(conditions.get("keyword").toString()))
            dto.setKeyword(conditions.get("keyword").toString());
        if(conditions.get("maxCount") != null && this.checkString(conditions.get("maxCount").toString()))
            dto.setMaxCount(Integer.parseInt(conditions.get("maxCount").toString()));
        if(conditions.get("orderByColumn") != null && this.checkString(conditions.get("orderByColumn").toString()))
            dto.setOrderByColumn(conditions.get("orderByColumn").toString());
        if(conditions.get("orderByDesc") != null && this.checkString(conditions.get("orderByDesc").toString()))
            dto.setOrderByDesc(Integer.parseInt(conditions.get("orderByDesc").toString()) == 0);
        if(conditions.get("page") != null && this.checkString(conditions.get("page").toString()))
            dto.setPage(Integer.parseInt(conditions.get("page").toString()));
        if(conditions.get("categoryId") != null && this.checkString(conditions.get("categoryId").toString()))
            dto.setCategoryId(Integer.parseInt(conditions.get("categoryId").toString()));
        return dto;
    }
}
