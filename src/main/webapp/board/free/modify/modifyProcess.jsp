<%@ page import="com.study.connection.entity.ContentsEntity" %>
<%@ page import="java.util.List" %>
<%@ page import="com.study.connection.DBActions" %>
<%@ page import="com.study.connection.CategoriesResults" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="com.study.connection.dao.ContentsDAO" %>
<%@ page import="com.study.connection.entity.FilesEntity" %>
<%@ page import="com.study.connection.dao.FilesDAO" %>
<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8" %>
<%
    try {

        ContentsEntity entities = new ContentsDAO("select" , "content_id=" + request.getParameter("content_id")).select().get(0);
        request.setAttribute("submit_date" , entities.getSubmitDate());
        request.setAttribute("title" , entities.getTitle());
        request.setAttribute("password" , entities.getPassword());
        request.setAttribute("view_count" , entities.getViewCount());
        request.setAttribute("content_id" , entities.getContentId());
        request.setAttribute("content" , entities.getContent());
        request.setAttribute("nickname" , entities.getNickname());
        request.setAttribute("update_date" , entities.getUpdateDate());
        request.setAttribute("content_category_id" , entities.getContentCategoryId());
        request.setAttribute("file_existence" , entities.getFileExistence());

    } catch (RuntimeException e) {
        e.getStackTrace();
    }

    //카테고리 제공
    try {
        request.setAttribute("category_name" , new CategoriesResults().categoriesSearch().get(request.getAttribute("content_category_id")));
    } catch (Exception e) {
        System.out.println("modifyCategorySearchError :   " + e.getMessage());
    }

    //파일이 존재할 때 content_id로 파일 내용을 가져온다.
    if((int) request.getAttribute("file_existence") == 1) {
        try {
            FilesEntity files = new FilesDAO("select" , "content_id=" + request.getAttribute("content_id")).select().get(0);
            request.setAttribute("files" , files);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    request.getRequestDispatcher(request.getRequestURL().toString().replace("http://localhost:8080" , "")
            .replace("modifyProcess.jsp" , "modify.jsp")).forward(request , response);
%>
