<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>자유 게시판 - 목록입니다.</title>
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
        function moveToView(contentId){
            let href = window.location.href;
            if(href.includes("index.jsp")) href = href.replace("index.jsp", "");
            if(href.includes("board/free") === false) href = href.replace("http://localhost:8080/eb-study-template-1week/", "http://localhost:8080/eb-study-template-1week/board/free/list/board.jsp")
            if (href.includes("contentId=") === false) {
                if(href.includes(".jsp?")) {
                    //?을 포함하면 검색을 한걸로 취급하고 &content_id를 붙인다.
                    return location.href = href.replace("http://localhost:8080" , "").replace("list/board.jsp" , "view/view.jsp") + "&contentId=" + contentId;
                }else{
                    //아니면 ?을 붙이고 content_id를 붙인다.
                    return location.href = href.replace("http://localhost:8080" , "").replace("list/board.jsp" , "view/view.jsp") + "?contentId=" +contentId;
                }
            }
        }
        function movePage(page){
            let href = window.location.href.replace("http://localhost:8080" , "");
            if (href.includes("index.jsp") == false) href = href + "index.jsp";
            href = href.replaceAll(/&?page=[0-9]+&?/gi , "");
            if (href.includes("?") && href.includes("&")) href = href + "&page=" + page;
            else{
                if (href.includes("?")){
                    const hrefSplit = href.split("?");
                    if(hrefSplit[1].length > 1) href = href + "&page=" + page;
                    else href = href + "page=" + page;
                }
                else href = href + "?page=" + page;
            }
            return location.href = href;
        }
    </script>

</head>
<body>
<script type="text/javascript">

function boardInitFetch() {
	fetch("board/free/list/boardProcess.jsp?status=init&page=1", {
		method: "GET",
	})
		.then((response) => response.text())
		.then((data) => {
			console.log(data);
			document.getElementById("init").insertAdjacentHTML("afterbegin", data);
		})
		.catch((err) => console.log(err));
}
function boardFetch() {
	fetch(window.location.href.replace("http://localhost:8080" , "").replace( "index.jsp", "board/free/list/boardProcess.jsp"), {
		method: "GET",
	})
		.then((response) => response.text())
		.then((data) => {
			console.log(data);
			document.getElementById("init").insertAdjacentHTML("afterbegin", data);
		})
		.catch((err) => console.log(err));
}
    function boardAjax() {
        $.ajax({
            type : "GET" , url : window.location.href.replace("http://localhost:8080" , "")
                .replace( "index.jsp", "board/free/list/boardProcess.jsp") ,
            success(data) {
                console.log(data);
                $("#init").html(data);
            }
        })
        $(this).off();
    }
    window.onload = function(){
    if(window.location.href.includes("?")){
        //쿼리스트링이 존재하는 경우 보통 검색 후에 view.jsp -> index.jsp 경로로 유입 되는 경우다.
        boardFetch()
    } else {
        //처음 화면이 아니더라도 init으로 동일하게 취급
        boardInitFetch()
    }}
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
    #pagination{
        display: flex;
        flex-direction: row;
        justify-content: center;
        gap: 1em;
        font-size: 14px;
    }
</style>
<div id="init">
    <%--board.jsp의 내용--%>
</div>
</body>
</html>
