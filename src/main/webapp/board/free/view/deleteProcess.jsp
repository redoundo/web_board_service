<%@ page import="com.study.connection.DBActions" %>
<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8" %>

<%
    request.setCharacterEncoding("UTF-8");
    String contentId = request.getParameter("content_id");

    try {
        new DBActions().updateDB("DELETE FROM contents WHERE content_id=" + contentId);
    } catch (Exception e) {
        System.out.println("DeleteProcessError :  " + e.getMessage());
    }

    RequestDispatcher requestDispatcher = request.getRequestDispatcher(request.getRequestURL().toString().replace("http://localhost:8080" , "").replace("view/deleteProcess.jsp" , "board/board.jsp"));
    requestDispatcher.forward(request , response);
%>