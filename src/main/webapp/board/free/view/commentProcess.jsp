<%@ page import="com.study.connection.entity.CommentsEntity" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.study.connection.dao.CommentsDAO" %>
<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8"%>

<%
    request.setCharacterEncoding("UTF-8");

    CommentsEntity entity = new CommentsEntity();
    entity.setCommentUser(request.getParameter("commentNickname"));
    entity.setComment(request.getParameter("commentText"));
    entity.setCommentedContentId(Integer.parseInt(request.getParameter("commentedContentId")));
    entity.setCommentedDate(new Date());

    try{
        new CommentsDAO(entity).update();
    }catch (Exception e) {
        e.getStackTrace();
    }

    request.getRequestDispatcher(request.getRequestURL().toString().replace("http://localhost:8080", "")
            .replace("commentProcess.jsp" , "view.jsp")).forward(request , response);
%>