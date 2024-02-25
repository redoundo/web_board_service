<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--커맨드 패턴, 멀티파트, 컨벤션 코드, document 주석, class 주석, 팩토리 패턴
    커맨드 패턴의 경우 객체를 만든다. 그걸 인터페이스로 만든다. list, view , update, insert가 인터페이스를 상속받는다.
    동일한 기점으로 실행할 수 있는 건 커맨드로 만들 수 있다.
    servlet 은 controller의 역할, 상속받은 것들은 moddel이다.->요청 자체의 구분은 커맨드로->모듈화, 디커플링.
    팩토리 패턴을 가지고 커맨드를
    parameter을 가지고 servlet으로 보내면 parameter을 가지고 서블릿이 판단해 커맨드를 가져온다.
--%>
<%--
    readme
    javadoc erd api문서  사용기술 및 도구 주요기능 사용자 페이지 관리자 페이지 로그인 인증
    쿼리는 간결하게 stream 사용.
--%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>자유 게시판 - 목록입니다.</title>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
</head>
<body>
<script type="text/javascript">
    function boardInitAjax() {
        $.ajax({
            type : "POST" , url : "board/free/list/boardProcess.jsp?status=init",
            success(data) {
                // 초기화면 정보 제공을 위해 boardProcess.jsp에서 처리한 내용을 담은 board.jsp를 data로 넘겨준다.
                $("#init").html(data);
            }
        })
        $(this).off();
    }
    function boardAjax() {
        $.ajax({
            type : "GET" , url : window.location.href.replace("http://localhost:8080" , "")
                .replace( "index.jsp", "board/free/list/boardProcess.jsp") ,
            success(data) {
                $("#init").html(data);
            }
        })
        $(this).off();
    }
    if(window.location.href.includes("?")){
        //쿼리스트링이 존재하는 경우 보통 검색 후에 view.jsp -> index.jsp 경로로 유입 되는 경우다.
        $(boardInitAjax);
    } else {
        //처음 화면이 아니더라도 init으로 동일하게 취급
        $(boardAjax);
    }
</script>
<div id="init">
    <%--board.jsp의 내용--%>
</div>
</body>
</html>
