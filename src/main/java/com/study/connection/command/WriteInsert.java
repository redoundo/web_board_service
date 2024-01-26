package com.study.connection.command;

import com.study.connection.controller.LoadFiles;
import com.study.connection.dto.ContentDto;
import com.study.connection.service.ContentsService;
import com.study.connection.service.FilesService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.util.*;

public class WriteInsert implements Command {
    @Override
    public void run(@NotNull HttpServletRequest req, HttpServletResponse res) throws Exception {

        try{
            req.setCharacterEncoding("UTF-8");
            ContentDto dto = ContentDto.builder()
                    .content(req.getParameter("content"))
                    .contentCategoryId(Integer.parseInt(req.getParameter("content_category_id")))
                    .title(req.getParameter("title"))
                    .submitDate(Date.valueOf(req.getParameter("submit_date")))
                    .password(req.getParameter("password"))
                    .viewCount(0)
                    .nickname(req.getParameter("nickname"))
                    .build();

            //request 에 존재하는 파일 이름들 리스트화
            List<Part> parts = new ArrayList<>(Arrays.asList(req.getPart("file1"),
                    req.getPart("file2"), req.getPart("file3")));
            String id = req.getParameter("content_id");

            new FilesService().insert(new LoadFiles().upload(parts , id)); // db 에 fileEntity insert 진행.
            new ContentsService().insert(dto);//db 에 contentEntity insert 진행

        } catch (Exception e) {
            System.out.println("WriteInsert.java :   " + e.getMessage());
            throw new Exception(e);
        }
    }
}
