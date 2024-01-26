<%@ page import="com.study.connection.entity.CommentsEntity" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ko">
<head>
    <title>게시판 보기</title>
</head>
<body>
<script type="text/javascript">
    function viewAjax() {
        $.ajax({
            type : "GET" , url : window.location.href.replace("http://localhost:8080" , "").replace("view.jsp" , "viewProcess.jsp")
        })
        $(this).off();
    }
    if(window.location.href.includes("content_id=")) {
        //content_id=는 board.jsp에서 특정 제목을 클릭해야 붙는다.
        $(viewAjax)
    }
</script>
<div id="data_center_detail">
    <h2>게시판 - 보기</h2>
    <div>
        <p><%=request.getParameter("nickname")%></p>
        <span>
                <p>등록일시: <%=request.getParameter("submit_date")%></p>
                <p>수정일시: <%=request.getParameter("update_date") == null? "" : request.getParameter("update_date")%></p>
            </span>
    </div>
    <div>
        <%--카테고리가 아닌 카테고리 아이디를 받아오는 상태. 즉, 추가적인 변환이 필요.--%>
        <h5>[<%=(request.getParameter("content_category_id"))%>]</h5>
        <h5><%=request.getParameter("title")%></h5>
        <p>조회수: <%=request.getParameter("view_count")%></p>
        <p><%=request.getParameter("content")%></p>
    </div>
    <div id="CommentBlock">
        <c:if test="<%=request.getAttribute("comments") != null%>">
            <c:forEach var="comments" items="<%=(List<CommentsEntity>) request.getAttribute("comments")%>">
                <div class="commentBlock">
                <span>
                    <p>${comments.commentUser}</p>
                    <p>${comments.commentedDate}</p>
                </span>
                    <p>${comments.comment}</p>
                </div>
            </c:forEach>
        </c:if>
        <div>
            <form action="commentProcess.jsp" method="get">
                <input type="hidden" name="commentedContentId" value="<%=request.getAttribute("content_id")%>"/>
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
</script>
</body>
</html>
