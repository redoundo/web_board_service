<%@ page import="com.study.connection.CategoriesResults" %>
<%@ page import="java.util.Map" %>
<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8"%>
<%
    request.setCharacterEncoding("UTF-8");
    //write.jsp의 처음 화면에 필요한 카테고리 내용들을 제공한다.
    try{
        Map<Integer,String> writeCategories = new CategoriesResults().categoriesSearch();
        request.setAttribute("categories" , writeCategories);
    }catch (RuntimeException e){
        System.out.println("writeProcessCategoryError :" + e.getMessage());
    }
    request.getRequestDispatcher(request.getRequestURL().toString().replace("http://localhost:8080/eb-study-template-1week" , "")
            .replace("getCategory.jsp" , "writeCategory.jsp")).include(request , response);
%>
