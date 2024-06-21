<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<html lang="ko">
<head>
    <title>게시판 - 수정</title>
    <link
                href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css"
                rel="stylesheet"
                integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9"
                crossorigin="anonymous" />
    <script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>

    <script type="text/javascript">
        function adjustAjax() {
            $.ajax({
                type : "GET" , url : window.location.href.replace("http://localhost:8080" , "").replace("modify.jsp" , "modifyProcess.jsp"),
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
        }
        if(window.location.href.includes("contentId=")) {
            $(document).ready(adjustAjax)
        }
    </script>
    <script type="text/javascript">
        //viewProcess.jsp로 이동한다.
        function toViewProcess () {
            return location.href = window.location.href.replace("http://localhost:8080" , "").replace("modify/modify.jsp" , "view/view.jsp");
        }
        function passwordAjax () {
            let provePassword = $("#proveForModify").attr("value");
            const originalPassword = $("#originalPassword").attr("value"); //db에서 가져온 password는 이미 단방향 암호화가 되어져 있다.
            //submit 여부를 통제하는 변수
            //TODO : onSubmit에서 false가 반환되면 submit이 되지 않는지 여부 확인이 필요하다.
            let valid = false;
            console.log(provePassword);
            console.log(originalPassword);
            $.ajax({
                type : "POST" , url : window.location.href.replace("http://localhost:8080/eb-study-template-1week" , "")
                    .replace("modify.jsp" , "encryptChecking.jsp") ,
                data : { forProve : provePassword },
                success (data) {
                    console.log(data);
                    if(data !== originalPassword) {
                        alert("정확한 비밀번호를 입력해주세요.")
                    }else {
                        valid = true;
                    }
                },
                error(error) {
                    console.log(error);
                    alert("잠시후 다시 시도해주세요.")
                }
            })
            return valid;
        }
    </script>
    <style>
        @font-face {
            font-family: "SUIT-Regular";
            src: url("https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_suit@1.0/SUIT-Regular.woff2")
                format("woff2");
            font-weight: normal;
            font-style: normal;
        }
        body{
            width: 100vw;
            height: 100vh;
            font-family: "SUIT-Regular";
            display: flex;
            flex-direction: row;
            justify-content: center;
        }
        #init{
            display: flex;
            height: 100%;
            width: 80%;
            flex-direction: column;
            gap: 10px;
            padding-top: 2em;
        }
    </style>
</head>
<body>
<div id="init">

</div>
</body>
</html>
