<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setCharacterEncoding("UTF-8");
    String contentId = request.getParameter("contentId");
    String href = request.getRequestURL().toString().replace("http://localhost:8080/eb-study-template-1week" , "").replace("modalProcess.jsp" , "forDelete.jsp");
    if(!href.matches("\\??contentId=[0-9]+")) href = href + "?contentId=" + contentId;
    System.out.println("href  : " + href);
    request.setAttribute("contentId", contentId);
    RequestDispatcher requestDispatcher = request.getRequestDispatcher(href);
    requestDispatcher.include(request , response);
%>