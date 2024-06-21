<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="com.study.connection.DBActions" %>
<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8"%>

<%
    //댓글을 가져오는 게 아니 등록할때 사용한다.
    request.setCharacterEncoding("UTF-8");
    String nickname = request.getParameter("commentNickname");
    String text = request.getParameter("commentText");
    String contentId = request.getParameter("commentedContentId");
    String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("y.M.d H:m"));
    String commentSql = "INSERT INTO comments(commentUser,comment,commentedDate,commentedContentId) VALUES('" +
            nickname + "','" + text + "',STR_TO_DATE('" + date + "','%Y.%m.%d %H:%i')," + contentId + ");";

    try{
        new DBActions().updateDB(commentSql);
    }catch (Exception e) {
        System.out.println("CommentInsertError :    " + commentSql + e.getMessage());
    }
    response.sendRedirect(request.getRequestURL().toString().replace("http://localhost:8080", "").replace("commentProcess.jsp" , "view.jsp") + "?contentId=" + contentId);
%>