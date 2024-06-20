<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<html lang="ko">
<head>
    <title>게시판 보기</title>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
    <script type="text/javascript">
        function writeAjax() {
            $.ajax({
                type : "GET" ,
                url : window.location.href.replace("http://localhost:8080" , "").replace("write.jsp" , "getCategory.jsp") ,
                success(data) {
                    $("#categorySelect").html(data); // 글 등록에 필요한 카테고리 제공을 위해 getCategory.jsp의 결과를 담은 writeCategory.jsp를 data로 제공.
                }
            })
            $(this).off();
        }
        $(writeAjax)

        function cancelWrite() {
            return location.href = window.location.href.replace("http://localhost:8080" , "").replace("board/free/write/write.jsp" ,"index.jsp")
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
        select {
            padding: 4px 6px;
        }
        p, label{
            margin: 0px;
            height: fit-content;
        }
        input {
            padding: 3px;
            padding-left: 7px;
            background-color: transparent;
            border: 1px solid transparent;
        }
        button {
            background-color: transparent;
            border: 1px solid darkseagreen;
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
        #buttons{
            display: flex;
            flex-direction: row;
            justify-content: center;
            gap: 2em;
            margin-top: 2em;
        }
        .btn{
            padding: 6px 20px;
            font-size: 14.5px;
            background-color: darkseagreen;
            border: 1px solid transparent;
            color: white;
        }
        .writeTableRows{
            display: flex;
            flex-direction: row;
            font-size: 14px;
            align-items: center;
            border-top: 1px solid darkseagreen;
            background-color: #f7f7f7;
        }
        .writeTableRowText{
            flex-basis: 25%;
            text-align: center;
            line-height: 2;
            padding: 5px;
        }
        .writeTableRowContentSpan{
            flex-basis: 75%;
            display: flex;
            flex-direction: row;
            gap: 15px;
            background-color: white;
            padding: 10px;
            padding-left: 15px;
            font-size: 13.5px;
        }
        .writeTableRowContentLabel{
            width: 30%;
        }
        .writeTableRowContent{
            width: 100%;
            border: 1px solid darkseagreen;
        }
    </style>
</head>
<body>
<div id="init">
    <h4 style="margin: 0px;margin-bottom: 2em;">게시판 - 등록</h4>
    <div>
        <div class="OptForm">
            <form id="OptionForm">
                <div>
                    <div class="writeTableRows">
                        <span class="writeTableRowText">카테고리*</span>
                        <span class="writeTableRowContentSpan">
                            <label class="writeTableRowContentLabel" for="categorySelect">
                                <select class="writeTableRowContent" id="categorySelect" name="writeCategory">
                                    <%--writeCategory.jsp의 내용--%>
                                </select>
                            </label>
                        </span>
                    </div>
                    <div class="writeTableRows">
                        <span class="writeTableRowText">작성자*</span>
                        <span class="writeTableRowContentSpan">
                            <label class="writeTableRowContentLabel" for="Writer">
                                <input class="writeTableRowContent" name="writeNickname" id="Writer" type="text" maxlength="4" minlength="3" required/>
                            </label>
                        </span>
                    </div>
                    <div class="writeTableRows">
                        <span class="writeTableRowText">비밀번호*</span>
                        <span class="writeTableRowContentSpan">
                            <label class="writeTableRowContentLabel">
                                <input class="writeTableRowContent" name="password" placeholder="비밀번호" type="password" required maxlength="15" minlength="4"/>
                            </label>
                            <label class="writeTableRowContentLabel" for="reProve">
                                <input class="writeTableRowContent" name="provePassword" id="reProve" placeholder="비밀번호 확인" type="password" required maxlength="15" minlength="4"/>
                            </label>
                        </span>
                    </div>
                    <div class="writeTableRows">
                        <span class="writeTableRowText">제목*</span>
                        <span class="writeTableRowContentSpan">
                            <label class="writeTableRowContentLabel" for="writeTitle">
                                <input class="writeTableRowContent" name="writeTitle" type="text" id="writeTitle" required maxlength="99" minlength="4"/>
                            </label>
                        </span>
                    </div>
                    <div class="writeTableRows">
                        <span class="writeTableRowText">내용*</span>
                        <span class="writeTableRowContentSpan">
                            <label class="writeTableRowContentLabel" for="writeContent">
                                <input class="writeTableRowContent" name="writeContent" type="text" maxlength="1999" minlength="4" id="writeContent"/>
                            </label>
                        </span>
                    </div>
                    <div class="writeTableRows" style="border-bottom: 1px solid darkseagreen;">
                        <span class="writeTableRowText">파일 첨부</span>
                        <span class="writeTableRowContentSpan" style="flex-direction: column;">
                            <label for="addFile1">
                                <input name="writeFile1" type="file" id="addFile1"/>
                            </label>
                            <label for="addFile2">
                                <input name="writeFile2" type="file" id="addFile2"/>
                            </label>
                            <label for="addFile3">
                                <input name="writeFile3" type="file" id="addFile3"/>
                            </label>
                        </span>
                    </div>
                </div>
            </form>
            <div id="buttons">
                <input class="btn" form="OptionForm" type="submit" formaction="/board/free/write/writeProcess.jsp" formmethod="post" value="저장"/>
                <button class="btn" type="button" onclick="cancelWrite()">취소</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
