<%@ page import="java.util.Objects" %>
<%@ page import="com.study.connection.dao.ContentsDAO" %>
<%@ page import="com.study.connection.entity.ContentsEntity" %>
<%@ page import="java.util.List" %>
<%@ page import="com.study.connection.entity.CommentsEntity" %>
<%@ page import="com.study.connection.dao.CommentsDAO" %>
<%@ page import="com.study.connection.entity.FilesEntity" %>
<%@ page import="com.study.connection.dao.FilesDAO" %>
<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8"%>

<%
    request.setCharacterEncoding("UTF-8");

    if(request.getParameter("content_id") != null) {
        //board.jsp에서 직접 들어왔을 때 content_id를 통해 내용을 가지고 오는 작업.
        String contentIdSql = "content_id=" + request.getParameter("content_id");
        try{
            ContentsEntity entity  = new ContentsDAO("select" , contentIdSql).select().get(0);
            request.setAttribute("title" , entity.getTitle());
            request.setAttribute("nickname" , entity.getNickname());
            request.setAttribute("content" , entity.getContent());
            request.setAttribute("password" , entity.getPassword());
            request.setAttribute("submit_date" , entity.getSubmitDate());
            request.setAttribute("update_date" , entity.getUpdateDate());
            //특정 view.jsp로 이동한 것이므로 조회수 +1 을 한다.
            request.setAttribute("view_count" , entity.getViewCount() + 1);
            request.setAttribute("content_category_id" , entity.getContentCategoryId());
            request.setAttribute("file_existence" , entity.getFileExistence());
            request.setAttribute("content_id" , entity.getContentId());
        } catch (Exception e) {
            e.getStackTrace();
        }

        // view_count + 1 을 db에 반영한다.
        ContentsEntity contents  = new ContentsEntity();
        contents.setViewCount((int)request.getAttribute("view_count"));
        try {
            new ContentsDAO( contents , "content_id=" + request.getAttribute("content_id")).update();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    } else {

        //writeProcess.jsp를 거쳐 write에서 작성된 내용을 출력하기 위해 왔다. write에서 작성된 내용들이 request.setAttribute에 온전하게 있을 거라는 가정하에 진행한다.
        String title = request.getAttribute("title").toString();
        String nickname = request.getAttribute("nickname").toString();
        String contentCategoryId = request.getAttribute("content_category_id").toString();
        String contentsSql = "title='" + title + "' AND nickname='" + nickname + "' AND content_category_id=" + contentCategoryId + ";";
        //전달되지 않은 값들을 db에서 찾아서 받아온다.
        try {
            ContentsEntity contentsEntity = new ContentsDAO("select" , contentsSql).select().get(0);
            request.setAttribute("content" , contentsEntity.getContent());
            request.setAttribute("view_count" , contentsEntity.getViewCount());
            request.setAttribute("file_existence" , contentsEntity.getFileExistence());
            request.setAttribute("content_id" , contentsEntity.getContentId());
            request.setAttribute("submit_date" , contentsEntity.getSubmitDate());
            request.setAttribute("update_date" , contentsEntity.getUpdateDate());
            request.setAttribute("password" , contentsEntity.getPassword());
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    if((int) request.getAttribute("file_existence") == 1) {
        //해당 content_id로 올라가져있는 파일이 있다면 가져온다.
        try{
            List<FilesEntity> files = new FilesDAO("select" , "content_id=" + request.getAttribute("content_id")).select();
            request.setAttribute("files" , files);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    //content_id로 해당 내용의 댓글을 가져온다.
    try {
        List<CommentsEntity> comments = new CommentsDAO("select" , "content_id=" + request.getAttribute("content_id")).select();
        request.setAttribute("comments" , comments);
    } catch (Exception e) {
        e.getStackTrace();
    }

    //request 세팅이 완료되었으면 view.jsp 로 돌아간다.
    request.getRequestDispatcher(request.getRequestURL().toString().replace("http://localhost:8080" , "")
            .replace("viewProcess.jsp" , "view.jsp")).forward( request , response );
%>