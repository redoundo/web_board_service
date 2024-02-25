<%@ page import="com.study.connection.DBActions" %>
<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8" %>

<%
    request.setCharacterEncoding("UTF-8");

    String content = request.getParameter("modifyContent");
    String user = request.getParameter("modifyWriter");
    String title = request.getParameter("modifyTitle");
    String content_id = request.getParameter("contentId");
    //TODO : 파일 첨부 기능과 변경된 파일의 수정 과정이 포함되어야한다.


    try {
        new DBActions().updateDB("UPDATE contents SET nickname='" +user + "',title='" + title + "',content='" + content + "',"
                + " WHERE content_id=" + content_id + ";");
    } catch (Exception e) {
        System.out.println("updateModifyError :   " + e.getMessage());
    }

    RequestDispatcher requestDispatcher = request.getRequestDispatcher(request.getRequestURL().toString().replace("http://localhost:8080" , "").replace("modify/updateModify.jsp" , "board/board.jsp"));
    requestDispatcher.forward(request , response);
%>