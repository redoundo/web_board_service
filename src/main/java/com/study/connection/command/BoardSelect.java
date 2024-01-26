package com.study.connection.command;

import com.study.connection.dao.CategoriesDao;
import com.study.connection.dao.ContentDao;
import com.study.connection.dto.ContentDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardSelect implements Command {
    @Override
    public void run(@NotNull HttpServletRequest req, HttpServletResponse res) throws Exception {

        try{

            req.setCharacterEncoding("UTF-8");
            String end = req.getParameter("end");
            String start = req.getParameter("start");
            String category = req.getParameter("category");
            String keyword = req.getParameter("keyword");
            String pageNumber = req.getParameter("page");

            Map<String , String> map = new HashMap<>();//
            map.put("end" , end);
            map.put("start" , start);
            map.put("content_category_id" , category);
            map.put("keyword" , keyword);
            map.put("page" , pageNumber);//limit page,10. default 1.

            ContentDao dao= new ContentDao();
            List<ContentDto> dtos = dao.select(map);//최대 10개만 존재.
            Integer total = dao.total();//위 조건의 전체 값. default 0.

            Map<Integer , String> categories = new CategoriesDao().select();

            req.setAttribute("content" , dtos);
            req.setAttribute("total" , total);
            req.setAttribute("category" , categories);


        } catch (Exception e) {
            System.out.println( "BoardSelect.java :    " + e.getMessage());
            throw new Exception(e);
        }


    }
}
