<%@ page import="com.study.connection.dto.ContentDto" %>
<%@ page import="com.study.connection.dao.ContentDao" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.study.connection.dao.CategoriesDao" %>
<%@ page import="com.study.connection.dto.FileDto" %>
<%@ page import="java.util.List" %>
<%@ page import="com.study.connection.service.FilesService" %>
<%@ page import="com.study.connection.entity.CommentEntity" %>
<%@ page import="com.study.connection.service.CommentsService" %>
<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html lang="ko">
<head>
    <title>게시판 - 보기</title>
    <meta charset="UTF-8">
</head>
<body>
<%
    request.setCharacterEncoding("UTF-8");
    //content_id는 없으면 안됨.
    String id = request.getParameter("content_id");

    Map<String , String > map = new HashMap<>();
    map.put("content_id" , id);

    ContentDto dao  = new ContentDao().select(map).get(0);//해당 content_id의 contents 내용
    Map<Integer , String> categories = new CategoriesDao().select();//카테고리 정보

    List<CommentEntity> comments;//해당 content_id로 가져온 comments 내용.
    try {
        comments = new CommentsService().select(id);
    } catch (Exception e) {
        throw new RuntimeException(e);
    }

    List<FileDto> files = null;
    try {
        files = new FilesService().select(id);
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }
%>
<div id="data_center_detail">
    <h2>게시판 - 보기</h2>
    <div>
        <p><%=dao.getNickname()%></p>
        <span>
            <p>등록일시: <%=dao.getSubmitDate()%></p>
            <p>수정일시: <%=dao.getUpdateDate() == null? "-" : dao.getUpdateDate()%></p>
        </span>
    </div>
    <div>
        <h5>[<%=categories.get(dao.getContentCategoryId())%>]</h5>
        <h5><%=dao.getTitle()%></h5>
        <p>조회수: <%=dao.getViewCount()%></p>
        <p><%=dao.getContent()%></p>
        <span>
            <c:if test='<%=files != null%>'>
                <c:forEach var="files" items='<%= files%>'>
                    <button type="button" onclick="download(${files.filePath} , ${files.fileName})"> ${files.fileName} </button>
                </c:forEach>
            </c:if>
        </span>
    </div>
    <div id="CommentBlock">
        <c:if test='<%=comments != null%>'>
            <c:forEach var="comment" items='<%=comments%>'>
                <div class="commentBlock">
                <div>
                    <p>${comment.commentUser}</p>
                    <p>${comment.commentedDate}</p>
                </div>
                    <p>${comment.comment}</p>
                </div>
            </c:forEach>
        </c:if>
        <div>
            <form action="main" method="get">
                <%--MainServlet command 제공 타입 : COMMENT_INSERT--%>
                <input type="hidden" name="Type" value="COMMENT_INSERT">
                <input type="hidden" name="commentedContentId" value='<%=request.getAttribute("content_id")%>'/>
                <label><input name="commentNickname" type="text"/></label>
                <label><input name="commentText" type="text" minlength="1" placeholder="댓글을 입력해 주세요"/></label>
                <input type="submit" value="등록"/>
            </form>
        </div>
    </div>
    <span>
            <button type="button" onclick="moveToBoard()">목록</button>
            <button type="button" onclick="goToAdjust()">수정</button>
            <button type="button" onclick="callDeleteModal()">삭제</button>
    </span>
</div>
<script type="text/javascript">
    //목록으로 되돌아간다. 특정 내용이 있는 페이지에서 벗어났기 때문에, content_id=를 삭제한다.
    function moveToBoard() {
        let rowHref = window.location.href;
        if(rowHref.includes("&")) {
            //content_id 하나만 있는 게 아닐 때.
            let lists = rowHref.split("?")[1].replace("&?content_id=[0-9]+" , "").split("&")
            return location.href = "/index.jsp?" + lists.join("&");
        } else {
            //content_id 하나만 있는 경우.
            return location.href = rowHref.replace("?content_id=[0-9]+" , "").replace("http://localhost:8080" , "")
                .replace("board/free/view/view.jsp" , "index.jsp");
        }
    }
    //수정 페이지로 넘어간다. location.href로 진행되기에 쿼리스트링에 포함된 content_id로 다시 한번 내용을 불러와야한다.
    function goToAdjust() {
        return location.href = window.location.href.replace("http://localhost:8080" , "").replace("view/view.jsp" , "modify/modify.jsp");
    }
    //삭제를 위한 비밀번호 확인 모달을 불러내나 임시적으로 페이지 이동을 하게끔 만들었다. 비밀번호,유효성 확인,board.jsp로의 이동은 전부 forDelete.jsp에서 이뤄진다.
    function callDeleteModal() {
        return location.href = window.location.href.replace("http://localhost:8080" , "").replace("view.jsp" , "forDelete.jsp");
    }
    function download(path , name) {
        if(path != null){
            $.ajax({
                type : "POST" , url : window.location.href.replace("http://localhost:8080" , "").replace("view.jsp" , "downloadFile.jsp") ,
                data : {path : path , name : name}
            })
        }
    }

</script>
</body>
</html>
