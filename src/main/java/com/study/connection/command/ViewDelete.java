package com.study.connection.command;

import com.study.connection.controller.PathMapper;
import com.study.connection.controller.ToView;
import com.study.connection.service.ContentsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;

public class ViewDelete implements Command {
    @Override
    public void run(HttpServletRequest req, @NotNull HttpServletResponse res) throws Exception {

        res.setContentType("text/html; charset=UTF-8");
        PrintWriter out = res.getWriter();

        try {

            new ContentsService().delete(req.getParameter("content_id"));

            out.println("<script>alert('게시물이 삭제되었습니다.')</script>");
            out.flush();

            String path = new PathMapper().mapper("board" ,
                    req.getRequestURL().toString());// view.jsp -> board.jsp
            new ToView().redirect(res , path);

        } catch (Exception e) {
            System.out.println("ViewDelete.java :  " + e.getMessage());
            throw new Exception(e);

        } finally {
            out.close();
        }
    }
}
