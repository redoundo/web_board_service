<%@ page import="com.study.connection.Encrypt" %>
<%@ page import="java.security.NoSuchAlgorithmException" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8" %>
<%
    request.setCharacterEncoding("UTF-8");
    String forProve = request.getParameter("forProve");
    try {
        String encrypted = new Encrypt().Encryption(forProve);
        PrintWriter writer = response.getWriter();
        writer.println(encrypted);//유효성 확인을 위해서 단방향 암호화된 비밀번호를 보낸다.
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
    }
%>