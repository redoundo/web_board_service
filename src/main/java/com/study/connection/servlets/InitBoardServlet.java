package com.study.connection.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet( urlPatterns = "/initServlet" , name = "initServlet" )
public class InitBoardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
        System.out.println("서블릿 호출 성공");
        req.setAttribute("blabla" , "블라블라");
    }
}
