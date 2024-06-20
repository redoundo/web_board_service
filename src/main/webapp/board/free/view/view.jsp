<%@ page import="com.study.connection.entity.CommentsEntity" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<html lang="ko">
<head>
    <title>게시판 보기</title>
    <link
                href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css"
                rel="stylesheet"
                integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9"
                crossorigin="anonymous" />
        <link rel="preconnect" href="https://fonts.googleapis.com" />
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
        <link
                href="https://fonts.googleapis.com/css2?family=Bebas+Neue&family=Lobster&family=Rubik+Mono+One&display=swap"
                rel="stylesheet" />
        <link
                rel="stylesheet"
                href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" />
        <link
                rel="stylesheet"
                href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" />
        <script
                src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
                integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm"
                crossorigin="anonymous"></script>

    <script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
    <script type="text/javascript">
        function viewAjax() {
            $.ajax({
                type : "GET" , url : window.location.href.replace("http://localhost:8080" , "").replace("view.jsp" , "viewProcess.jsp"),
               success(data) {
                   console.log(data);
                   document.getElementById("init").insertAdjacentHTML("afterbegin", data);
               },
               error(request,status, error){
                    console.log(error);
                    console.log(request);
                    console.log(status);
               }
            })
            $(this).off();
        }
        if(window.location.href.includes("contentId=")) {
            //content_id=는 board.jsp에서 특정 제목을 클릭해야 붙는다.
            $(document).ready(viewAjax)
        }
    </script>
    <script type="text/javascript">
        //목록으로 되돌아간다. 특정 내용이 있는 페이지에서 벗어났기 때문에, content_id=를 삭제한다.
        function moveToBoard() {
            let rowHref = window.location.href;
            if(rowHref.includes("&")) {
                //content_id 하나만 있는 게 아닐 때.
                let lists = rowHref.split("?")[1].replace(/&?\??contentId=[0-9]+/g , "").split("&")
                return location.href = "/index.jsp?" + lists.join("&");
            }
            if(rowHref.includes("?")) {
                //content_id 하나만 존재하는 경우.
                return location.href = rowHref.replace(/\??contentId=[0-9]+/g, "").replace("http://localhost:8080" , "")
                    .replace("board/free/view/view.jsp" , "index.jsp");
            }
            return location.href = rowHref.replace("http://localhost:8080" , "").replace("board/free/view/view.jsp" , "")
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
</head>
<body>
<div id="init">

</div>
</body>
</html>
