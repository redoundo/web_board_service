<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<h4 style="margin: 0px;margin-bottom: 2em;">게시판 - 수정</h4>
<div class="OptForm">
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
            margin-top: 1.5em;
        }
        .btn{
            padding: 6px 20px;
            font-size: 14.5px;
            background-color: darkseagreen;
            border: 1px solid transparent;
            color: white;
        }
        .modifyTableRows{
            display: flex;
            flex-direction: row;
            font-size: 14px;
            align-items: center;
            border-top: 1px solid darkseagreen;
            background-color: #f7f7f7;
        }
        .modifyTableRowText{
            flex-basis: 25%;
            text-align: center;
            line-height: 2;
            padding: 5px;
        }
        .modifyTableRowContentSpan{
            flex-basis: 75%;
            display: flex;
            flex-direction: row;
            gap: 15px;
            background-color: white;
            padding: 10px;
            padding-left: 15px;
            font-size: 13.5px;
        }
        .modifyTableRowContentLabel{
            width: 30%;
        }
        .modifyTableRowContent{
            width: 100%;
            border: 1px solid darkseagreen;
        }
        #modifyFileContent{
            display: flex;
            flex-direction: column;
            background-color: white;
            flex-basis: 75%;
            padding: 10px;
            gap: 10px;
        }
    </style>
    <form id="ModifyForm" enctype="multipart/form-data">
        <div>
            <div class="modifyTableRows">
                <span class="modifyTableRowText">카테고리*</span>
                <span class="modifyTableRowContentSpan">
                    <span class="modifyTableRowContentLabel">
                        <%=request.getAttribute("categoryName")%>
                    </span>
                </span>
            </div>
            <div class="modifyTableRows">
                <span class="modifyTableRowText">등록 일시</span>
                <span class="modifyTableRowContentSpan">
                    <span class="modifyTableRowContentLabel">
                        <%=request.getAttribute("submitDate")%>
                    </span>
                </span>
            </div>
            <div class="modifyTableRows">
                <span class="modifyTableRowText">수정 일시</span>
                <span class="modifyTableRowContentSpan">
                    <span class="modifyTableRowContentLabel">
                        <%=request.getAttribute("updateDate")%>
                    </span>
                </span>
            </div>
            <div class="modifyTableRows">
                <span class="modifyTableRowText">작성자*</span>
                <span class="modifyTableRowContentSpan">
                    <label class="modifyTableRowContentLabel" for="Writer">
                        <input value='<%=request.getAttribute("nickname")%>' name="modifyWriter" id="Writer"
                               type="text" maxlength="4" minlength="3" required/>
                    </label>
                </span>
            </div>
            <div class="modifyTableRows">
                <span class="modifyTableRowText">비밀번호 확인*</span>
                <span class="modifyTableRowContentSpan">
                    <label class="modifyTableRowContentLabel">
                        <input name="contentId" type="hidden" value='<%=request.getAttribute("contentId")%>'/>
                        <input id="originalPassword" type="hidden" value='<%=request.getAttribute("password")%>'/>
                        <input id="proveForModify" placeholder="비밀번호" type="password"
                               required maxlength="15" minlength="4"/>
                    </label>
                </span>
            </div>
            <div class="modifyTableRows">
                <span class="modifyTableRowText">제목*</span>
                <span class="modifyTableRowContentSpan">
                    <label class="modifyTableRowContentLabel" for="writeTitle">
                        <input value='<%=request.getAttribute("title")%>' name="modifyTitle" type="text"
                               id="writeTitle" required maxlength="99" minlength="4"/>
                    </label>
                </span>
            </div>
            <div class="modifyTableRows">
                <span class="modifyTableRowText">내용*</span>
                <span class="modifyTableRowContentSpan">
                    <label class="modifyTableRowContentLabel" for="writeContent">
                        <input value='<%=request.getAttribute("content")%>' name="modifyContent" type="text"
                               maxlength="1999" minlength="4" id="writeContent" required/>
                    </label>
                </span>
            </div>
            <div class="modifyTableRows">
                <span class="modifyTableRowText">파일 첨부</span>
                <div id="modifyFileContent">
                    <c:choose>
                        <c:when test='<%=request.getAttribute("fileExistence") != null%>'>
                            <c:set var="files" value='<%=(List<Map<String,String>>) request.getAttribute("files")%>'/>
                            <c:forEach var="index" begin="1" end="${files.size()}" step="1">
                                <span>
                                    <i class="bi bi-download"></i>
                                    <a download='${files.get(index - 1).get("filePath")}'>${files.get(index - 1).get("fileName")}</a>
                                </span>
                            </c:forEach>
                            <c:if test="${3 - files.size() > 0}">
                                <c:forEach var="index" begin="1" end="${3 - files.size()}" step="1">
                                    <span>
                                        <input name='modify_file${index}' type="file"/>
                                    </span>
                                </c:forEach>
                            </c:if>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="index" begin="1" end="3" step="1">
                                <span>
                                    <input name='modify_file${index}' type="file" id='addFile${index}'/>
                                </span>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </form>
    <span id="buttons">
        <button class="btn" type="button" onclick="toViewProcess()">취소</button>
        <input class="input apply btn" onsubmit="passwordAjax()" form="ModifyForm" type="submit"
               formaction='<%=request.getRequestURL().toString().replace("http://localhost:8080" , "").replace("modifyContent.jsp" , "updateModify.jsp").replace("modify.jsp" , "updateModify.jsp")%>'
               formmethod="post" value="저장"/>
    </span>
</div>