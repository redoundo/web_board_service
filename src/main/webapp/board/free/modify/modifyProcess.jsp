<%@ page import="com.study.connection.entity.ContentsEntity" %>
<%@ page import="java.util.List" %>
<%@ page import="com.study.connection.DBActions" %>
<%@ page import="com.study.connection.CategoriesResults" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Arrays" %>
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%
    //    if (request.getParameter("end") != null || request.getParameter("start") != null) {
//        if (request.getParameter("end") != null && request.getParameter("start") != null) {
//            params.add("submit_date BETWEEN " + "STR_TO_DATE('" + request.getParameter("start") + "','%Y.%m.%d %H:%i') AND STR_TO_DATE('" + request.getParameter("end") + "','%Y.%m.%d %H:%i')");
//        } else {
//            params.add("submit_date=STR_TO_DATE('" + request.getParameter("start") == null ? request.getParameter("end") : request.getParameter("start"));
//        }
//    } else {
//        for (String param : request.getParameterMap().keySet()) {
//            String paramValue = request.getParameter(param);
//            if (Objects.equals(param, "category")) {
//                //태그 이름은 category이지만 실제로는 int인 content_category_id로 int는 '' 있으면 오류가 나기 때문에 따로 처리.
//                if (!paramValue.isEmpty()) {
//                    //전체 카테고리는 빈 문자열이고 조건문으로 넣을 필요가 없다.
//                    params.add("content_category_id=" + paramValue);
//                }
//            } else if (Objects.equals(param, "keyword")) {
//                params.add("title like '%" + paramValue + "%'");
//            } else {
//                //content_id일 경우에만 해당.
//                params.add(param + "=" + paramValue);
//            }
//        }
//    }

    try {
        System.out.println("contentId : " + request.getParameter("contentId"));
        //content_id는 반드시 존재해야한다.
        List<ContentsEntity> entities = new DBActions().returnFullContents("contentId=" + request.getParameter("contentId"));
        ContentsEntity content = entities.get(0);
        request.setAttribute("submitDate" , content.getSubmitDate());
        request.setAttribute("title" , content.getTitle());
        request.setAttribute("password" , content.getPassword());
        request.setAttribute("viewCount" , content.getViewCount());
        request.setAttribute("contentId" , content.getContentId());
        request.setAttribute("content" , content.getContent());
        request.setAttribute("nickname" , content.getNickname());
        request.setAttribute("updateDate" , content.getUpdateDate());
        request.setAttribute("contentCategoryId" , content.getContentCategoryId());
        request.setAttribute("fileExistence" , content.getFileExistence());
    } catch (RuntimeException e) {
        System.out.println("modifyInitSearchError :   " + e.getMessage());
    }

    try {
        request.setAttribute("categoryName" , new CategoriesResults().categoriesSearch().get(request.getAttribute("contentCategoryId")));
    } catch (Exception e) {
        System.out.println("modifyCategorySearchError :   " + e.getMessage());
    }

    if(Boolean.parseBoolean(request.getAttribute("fileExistence").toString())) {
        //파일이 존재할 때 content_id로 파일 경로를 가져온다.
        try {
            String sql = "SELECT fileId,fileName,filePath,fileExtension FROM files WHERE contentIdHaveFile="
                    + request.getAttribute("contentId") + ";";
            List<Map<String,String>> files = new DBActions().conditionalSearch(sql
                    , new ArrayList<>(Arrays.asList("fileId" , "fileName" , "filePath" , "fileExtension")));
            request.setAttribute("files" , files);
        } catch (Exception e) {
            System.out.println("modifyProcessWhileGetFile :   " + e.getMessage());
        }
    }

    RequestDispatcher requestDispatcher = request.getRequestDispatcher(request.getRequestURL().toString().replace("http://localhost:8080/eb-study-template-1week" , "").replace("modifyProcess.jsp" , "modifyContent.jsp"));
    requestDispatcher.forward(request,response);
%>
