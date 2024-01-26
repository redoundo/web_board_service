package com.study.connection.command;

import com.study.connection.dao.CategoriesDao;
import com.study.connection.dao.ContentDao;
import com.study.connection.dto.ContentDto;
import com.study.connection.dto.FileDto;
import com.study.connection.service.FilesService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModifySelect implements Command{
    @Override
    public void run(HttpServletRequest req, HttpServletResponse res) throws Exception {
        try{
            req.setCharacterEncoding("UTF-8");

            String content= req.getParameter("content_id");
            Map<String , String> map = new HashMap<>();
            map.put("content_id" , content);

            ContentDao dao= new ContentDao();
            ContentDto dtos = dao.select(map).get(0);

            Map<Integer , String> categories = new CategoriesDao().select();

            req.setAttribute("content" , dtos);
            req.setAttribute("category" , categories);

            List<FileDto> files = new FilesService().select(content);
            req.setAttribute("files" , files);

        } catch (Exception e) {
            System.out.println("ModifySelect.java :   " + e.getMessage());
            throw new Exception(e);
        }
    }
}
