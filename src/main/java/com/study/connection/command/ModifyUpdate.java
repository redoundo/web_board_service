package com.study.connection.command;

import com.study.connection.controller.LoadFiles;
import com.study.connection.controller.PathMapper;
import com.study.connection.controller.ToView;
import com.study.connection.dto.ContentDto;
import com.study.connection.service.ContentsService;
import com.study.connection.service.FilesService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class ModifyUpdate implements Command {
    @Override
    public void run(@NotNull HttpServletRequest req, HttpServletResponse res) throws Exception {

        req.setCharacterEncoding("UTF-8");
        try {
            ContentDto dto = ContentDto.builder()
                .contentId(Integer.parseInt(req.getParameter("content_id")))
                .nickname(req.getParameter("nickname"))
                .title(req.getParameter("title"))
                .content(req.getParameter("content"))
                .build();

            List<Part> parts = new ArrayList<>(Arrays.asList(req.getPart("file1"),
                    req.getPart("file2"), req.getPart("file3")));
            String id = req.getParameter("content_id");
            new FilesService().insert(new LoadFiles().upload(parts , id)); // db 에 fileEntity insert 진행.
            new ContentsService().update(dto);

        } catch (Exception e) {
            System.out.println("ModifyUpdate.java :   " + e.getMessage());
            throw new Exception(e);
        }
    }
}
