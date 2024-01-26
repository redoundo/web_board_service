package com.study.connection.controller;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * request.getRequestDispatcher 의 경로 지정시의 난잡함을 줄이기 위해 존재.
 * paths 는 dispatcher 가 이동할 만한 경로를 제공. 현재 경로를 기반으로 경로를 변경시킨다.
 * mapper 는 현재의 경로를 기반으로 이동을 원하는 경로로 바꾼 내용을 제공.
 */
public class PathMapper {

    @NotNull
    private Map<String , String> paths() {
        Map<String , String> map = new HashMap<>();
        map.put("board" , "/board/free/list/board.jsp");
        map.put("index" , "/index.jsp");
        map.put("modify" , "/board/free/modify/modify.jsp");
        map.put("view" , "/board/free/view/view.jsp");
        map.put("write" , "/board/free/write/write.jsp");
        return map;
    }

    public String mapper(String to , @NotNull String now) {
        //to 는 이동하기 원하는 곳. now 는 현재 url 을 의미한다.
        //TODO : application.properties 의 SITE 를 ""으로 replace 시켜야하지만 가져오는 방법을 모르겠다...
        if(now.contains("?")){
            String query = now.split("\\?")[1];
            return this.paths().get(to) + "?" + query;
        } else{
            return this.paths().get(to);
        }
    }

    public String mapping (String to ,  @Nullable Map<String , String> query) {
        String path = this.paths().get(to);

        List<String> queries = new ArrayList<>();
        if(query != null) {
            for(Object param : query.keySet().toArray()) {
                queries.add(param.toString() + "=" + query.get(param.toString()));
            }
        }

        path = queries.isEmpty() ? path : path + "?" + String.join("&" , queries) ;
        return  path;
    }

}
