<%@ page import="com.study.connection.dao.ContentsDAO" %>
<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8" %>

<%
    request.setCharacterEncoding("UTF-8");
    String contentId = request.getParameter("content_id");

    try {
        new ContentsDAO("delete" , "content_id=" + contentId).update();
    } catch (Exception e) {
        e.getStackTrace();
    }

    request.getRequestDispatcher(request.getRequestURL().toString().replace("http://localhost:8080" , "")
            .replace("view/deleteProcess.jsp" , "board/board.jsp")).forward(request , response);
%>