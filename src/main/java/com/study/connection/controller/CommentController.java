package com.study.connection.controller;

import com.study.connection.entity.CommentEntity;
import com.study.connection.service.CommentsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.List;

/**
 *
 */
@WebServlet(urlPatterns = "/board/list/view/*/CommentController" , name = "CommentController")
public class CommentController  extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        super.doGet(req, res);

        try {

            String id = req.getParameter("content_id");
            List<CommentEntity> comments = new CommentsService().select(id);

            req.setAttribute("comments" , comments);

            String path = new PathMapper().mapper("view" , req.getRequestURL().toString());
            new ToView().include(req , res , path);

        } catch (Exception e) {

            res.setContentType("text/html; charset=UTF-8");
            PrintWriter out = res.getWriter();
            out.println("<script>alert('내용을 가져오는데 오류가 발생했습니다.\n" +
                    "새로고침 해주시기 바랍니다.')</script>");
            out.flush();
            out.close();

        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        super.doPost(req, res);
        //insert 용도
        CommentEntity entity = CommentEntity.builder()
                .commentUser(req.getParameter("comment_user"))
                .comment(req.getParameter("comment"))
                .commentedDate(Date.valueOf(req.getParameter("commented_date")))
                .commentedContentId(Integer.parseInt(req.getParameter("commented_content_id")))
                .build();
        try{

            new CommentsService().insert(entity);

            String path = new PathMapper().mapper("view" , req.getRequestURL().toString());
            new ToView().include(req , res , path);

        } catch (Exception e){
            res.setContentType("text/html; charset=UTF-8");
            PrintWriter out = res.getWriter();
            out.println("<script>alert('내용을 저장하는데 오류가 발생했습니다.\n" +
                    "잠시 후 다시 시도해주시기 바랍니다.')</script>");
            out.flush();
            out.close();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        super.doDelete(req, res);
        //delete 용도
        try{
            String id = req.getParameter("commented_content_id");
            new CommentsService().delete(id);

            String path = new PathMapper().mapper("board" , req.getRequestURL().toString());
            new ToView().redirect(res , path);

        } catch (Exception e) {
            res.setContentType("text/html; charset=UTF-8");
            PrintWriter out = res.getWriter();
            out.println("<script>alert('내용을 삭제하는데 오류가 발생했습니다.\n" +
                    "잠시 후 다시 시도해주시기 바랍니다.')</script>");
            out.flush();
            out.close();
        }
    }
}
