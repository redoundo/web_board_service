package com.study.connection.controller;

import com.study.connection.factory.CommandFactory;
import com.study.connection.factory.CommandName;
import com.study.connection.factory.Factory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = "/" , name = "main")
public class MainServlet extends HttpServlet {

    Factory factory = new CommandFactory();

    @Override
    protected void doGet (@NotNull HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        //super.doGet(req, resp);
        req.setCharacterEncoding("UTF-8");
        String type = req.getParameter("Type");
        if (type != null && !type.isEmpty()) {
            try{
                factory.getCommand(type).run(req , res);
                //command 들에서 바로 requestDispatcher 사용하지 않고 처리한 뒤에 mainServlet 에서 보내줌.
                if(type.equals(CommandName.MODIFY_UPDATE.name())) {
                    Map<String , String> query = new HashMap<>();
                    query.put("content_id" , req.getParameter("content_id"));
                    String path = new PathMapper().mapping("view" , query);//modify.jsp -> 내용이 변경된 view.jsp
                    new ToView().redirect(res , path);//view.jsp 로 redirect.
                } else if (type.equals(CommandName.COMMENT_INSERT.name())) {
                    String path = new PathMapper().mapper("view" , req.getRequestURL().toString());
                    new ToView().include(req , res , path);
                } else if (type.equals(CommandName.BOARD_SELECT.name())) {
                    String path = new PathMapper().mapper("board" , req.getRequestURL().toString());
                    new ToView().include(req , res ,path);
                } else if (type.equals(CommandName.MODIFY_SELECT.name())) {
                    String path = new PathMapper().mapper("modify" , req.getRequestURL().toString());
                    new ToView().forward(req , res ,path);
                } else if (type.equals(CommandName.VIEW_DELETE.name())) {
                    String path = new PathMapper().mapper("board" , req.getRequestURL().toString());// view.jsp -> board.jsp
                    new ToView().redirect(res , path);
                } else if (type.equals(CommandName.VIEW_SELECT.name())) {
                    String path = new PathMapper().mapper("view" , req.getRequestURL().toString());
                    new ToView().forward(req , res ,path);
                } else if (type.equals(CommandName.WRITE_INSERT.name())) {
                    Map<String , String> query = new HashMap<>();
                    query.put("title" , req.getParameter("title"));
                    query.put("nickname" , req.getParameter("nickname"));
                    query.put("content_category_id" , req.getParameter("content_category_id"));

                    String path = new PathMapper().mapping("view" , query); //write.jsp -> view.jsp
                    new ToView().redirect(res , path);//query 로 찾은 내용이 있을 view.jsp 로 이동.
                } else{
                    throw new Exception();
                }
            } catch (Exception e) {
                System.out.println("MainServlet.java :    " + e.getMessage());
                res.setContentType("text/html; charset=UTF-8");
                PrintWriter out = res.getWriter();
                out.println("<script>alert('진행하는데 오류가 발생했습니다." +
                        "잠시 뒤에 다시 시도해주시기 바랍니다.')</script>");
                out.flush();
                out.close();
            }
        }
    }

    @Override
    protected void doPost(@NotNull HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        //super.doPost(req, resp);
        req.setCharacterEncoding("UTF-8");
        String type = req.getParameter("Type");
        if (type != null && !type.isEmpty()) {
            try{
                factory.getCommand(type).run(req , res);
                //command 들에서 바로 requestDispatcher 사용하지 않고 처리한 뒤에 mainServlet 에서 보내줌.
                if(type.equals(CommandName.MODIFY_UPDATE.name())) {
                    Map<String , String> query = new HashMap<>();
                    query.put("content_id" , req.getParameter("content_id"));
                    String path = new PathMapper().mapping("view" , query);//modify.jsp -> 내용이 변경된 view.jsp
                    new ToView().redirect(res , path);//view.jsp 로 redirect.
                } else if (type.equals(CommandName.COMMENT_INSERT.name())) {
                    String path = new PathMapper().mapper("view" , req.getRequestURL().toString());
                    new ToView().include(req , res , path);
                } else if (type.equals(CommandName.BOARD_SELECT.name())) {
                    String path = new PathMapper().mapper("board" , req.getRequestURL().toString());
                    new ToView().include(req , res ,path);
                } else if (type.equals(CommandName.MODIFY_SELECT.name())) {
                    String path = new PathMapper().mapper("modify" , req.getRequestURL().toString());
                    new ToView().forward(req , res ,path);
                } else if (type.equals(CommandName.VIEW_DELETE.name())) {
                    String path = new PathMapper().mapper("board" , req.getRequestURL().toString());// view.jsp -> board.jsp
                    new ToView().redirect(res , path);
                } else if (type.equals(CommandName.VIEW_SELECT.name())) {
                    String path = new PathMapper().mapper("view" , req.getRequestURL().toString());
                    new ToView().forward(req , res ,path);
                } else if (type.equals(CommandName.WRITE_INSERT.name())) {
                    Map<String , String> query = new HashMap<>();
                    query.put("title" , req.getParameter("title"));
                    query.put("nickname" , req.getParameter("nickname"));
                    query.put("content_category_id" , req.getParameter("content_category_id"));

                    String path = new PathMapper().mapping("view" , query); //write.jsp -> view.jsp
                    new ToView().redirect(res , path);//query 로 찾은 내용이 있을 view.jsp 로 이동.
                } else{
                    throw new Exception();
                }
            } catch (Exception e) {
                res.setContentType("text/html; charset=UTF-8");
                PrintWriter out = res.getWriter();
                out.println("<script>alert('진행하는데 오류가 발생했습니다." +
                        "잠시 뒤에 다시 시도해주시기 바랍니다.')</script>");
                out.flush();
                out.close();
            }
        }
    }

    @Override
    protected void doPut(@NotNull HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        //super.doPut(req, resp);
        req.setCharacterEncoding("UTF-8");
        String type = req.getParameter("Type");

    }

    @Override
    protected void doDelete(@NotNull HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        //super.doDelete(req, resp);
        req.setCharacterEncoding("UTF-8");
        String type = req.getParameter("Type");

    }
}
