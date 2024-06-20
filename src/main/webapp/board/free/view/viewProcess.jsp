<%@ page import="com.study.connection.entity.ContentsEntity" %>
<%@ page import="java.util.Objects" %>
<%@ page import="java.util.List" %>
<%@ page import="com.study.connection.DBActions" %>
<%@ page import="com.study.connection.entity.CommentsEntity" %>
<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8"%>
<%
    request.setCharacterEncoding("UTF-8");
    if(request.getParameter("contentId") != null) {
        //board.jsp에서 직접 들어왔을 때 content_id를 통해 내용을 가지고 오는 작업.
        String contentIdSql = null;
        for (String param : request.getParameterMap().keySet()){
            if(Objects.equals(param , "contentId")) {
                contentIdSql = "contentId=" + request.getParameter("contentId");
            }
        }
        System.out.println("contentIdSql :   " + contentIdSql);
        try {
            List<ContentsEntity> entityList = new DBActions().returnFullContents(contentIdSql);
            if(entityList.size() == 1) {
                ContentsEntity entity = entityList.get(0);
                request.setAttribute("title" , entity.getTitle());
                request.setAttribute("nickname" , entity.getNickname());
                request.setAttribute("content" , entity.getContent());
                request.setAttribute("password" , entity.getPassword());
                request.setAttribute("submitDate" , entity.getSubmitDate());
                request.setAttribute("updateDate" , entity.getUpdateDate());
                //특정 view.jsp로 이동한 것이므로 조회수 +1 을 한다.
                request.setAttribute("viewCount" , entity.getViewCount() + 1);
                System.out.println("getContentCategoryId :   " + entity.getContentCategoryId());
                request.setAttribute("contentCategoryId" , entity.getContentCategoryId());
                request.setAttribute("fileExistence" , entity.getFileExistence());
                request.setAttribute("contentId" , entity.getContentId());
            }
        } catch (RuntimeException e) {
            //TODO : 이럴 경우 어떤 값을 반환해야하는건지, 아니면 다시 값을 가져와야 하는건지 정하지 않았다.
            System.out.println("ViewContentIdInitError :    " + e.getMessage());
        }
        //업데이트된 viewCount를 db에 반영한다.
        try {
            String updateSql = "UPDATE  contents SET viewCount=" + request.getAttribute("viewCount").toString() +  " WHERE " + contentIdSql + ";";
            new DBActions().updateDB(updateSql);
        }catch (RuntimeException e) {
            System.out.println("UpdateViewCountError :  " + e.getMessage());
        }
    } else {
        //writeProcess.jsp를 거쳐 write에서 작성된 내용을 출력하기 위해 왔다. write에서 작성된 내용들이 request.setAttribute에 온전하게 있을 거라는 가정하에 진행한다.
        String title = request.getAttribute("title").toString();
        String nickname = request.getAttribute("nickname").toString();
        String contentCategoryId = request.getAttribute("contentCategoryId").toString();
        String contentsSql = "title='" + title + "' AND nickname='" + nickname + "' AND contentCategoryId=" + contentCategoryId + ";";
        //전달되지 않은 값들을 db에서 찾아서 받아온다.
        try{
            List<ContentsEntity> contentsEntities = new DBActions().returnFullContents(contentsSql);
            if(contentsEntities.size() == 1) {
                ContentsEntity contents = contentsEntities.get(0);
                request.setAttribute("content" , contents.getContent());
                request.setAttribute("viewCount" , contents.getViewCount());
                request.setAttribute("fileExistence" , contents.getFileExistence());
                request.setAttribute("contentId" , contents.getContentId());
                request.setAttribute("submitDate" , contents.getSubmitDate());
                request.setAttribute("updateDate" , contents.getUpdateDate());
                request.setAttribute("password" , contents.getPassword());
            }
        }catch (RuntimeException e) {
            System.out.println("TitleNicknameContentCategoryIdError :   " + e.getMessage());
        }
    }
    try{
        List<CommentsEntity> entities = new DBActions().ReturnFullComments(request.getAttribute("contentId").toString());
        request.setAttribute("comments" , entities);
    } catch (RuntimeException e) {
        System.out.println("GetCommentsError :  " + e.getMessage());
        request.setAttribute("comments" , null);
    }

    try {
        System.out.println("categoryName contentCategoryId :   " + request.getAttribute("contentCategoryId"));
        String categoryName = new DBActions().aCategory(Integer.parseInt(request.getAttribute("contentCategoryId").toString()));
        request.setAttribute("categoryName" , categoryName);
    }catch (RuntimeException e) {
         System.out.println("categoryName Error :   " + e.getMessage());
     }
    //처리를 완료했다면 view.jsp로 가서 내용을 출력한다.
    RequestDispatcher requestDispatcher = request.getRequestDispatcher(request.getRequestURL().toString().replace("http://localhost:8080/eb-study-template-1week" , "").replace("viewProcess.jsp" , "viewContent.jsp"));
    requestDispatcher.forward(request,response);
%>