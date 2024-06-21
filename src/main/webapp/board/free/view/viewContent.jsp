<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.study.connection.entity.CommentsEntity" %>
<%@ page import="java.util.List" %>
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
        display: flex;
        font-family: "SUIT-Regular";
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
    #categoryAndTitle{
        display: flex;
        flex-direction: row;
        font-size: 16px;
        gap: 1em;
        border-bottom: 2px solid darkseagreen;
        padding-bottom: 10px;
    }
    #categoryNameDiv{
        font-weight: 900;
        flex-basis: 10%;
        text-align: center;
    }
    #titleDiv{
        font-weight: 900;
        flex-basis: 90%;
    }
    #elseDetailsSpan{
        display: flex;
        flex-direction: row;
        font-size: 14px;
        padding: 10px 5px;
        justify-content: space-between;
    }
    #nicknameSpan{
        flex-basis: 10%;
        text-align: center;
    }
    #elseDetails{
        display: flex;
        flex-direction: row;
        gap: 20px;
        margin-right: 10px;
    }
    #contentText{
        padding: 0px 15px;
        font-size: 15px;
        min-height: 50px;
        margin: 15px;
    }
    #commentForm{
        display: flex;
        flex-direction: column;
        gap: 10px;
        padding: 1em;
        background-color: #f7f7f7;
        font-size: 14px;
        margin: 0px;
    }
    #nicknameAndSave{
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        margin: 0px 20px;
    }
    #nicknameInput{
        border-bottom: 1px solid darkseagreen;
        padding: 4px;
    }
    #saveBtn{
        font-size: 13.5px;
        background-color: darkseagreen;
        border: 1px solid transparent;
        color: white;
        padding: 5px 15px;
    }
    #textareaLabel{
        padding: 0px 20px 5px 20px;
        font-size: 13px;
    }
    #textareaContent{
        width: 100%;
        resize: none;
        background-color: white;
        border: 1px solid transparent;
        padding: 10px 15px;
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
    .commentBlock{
        display: flex;
        flex-direction: column;
        gap: 10px;
        padding: 1em;
        background-color: #f7f7f7;
        font-size: 14px;
        border-top: 1px solid darkseagreen;
    }
    .userAndDate{
        display: flex;
        gap: 15px;
        margin: 0px 20px;
        flex-direction: row;
        align-items: center;
    }
    .commentUser{
        font-size: 13px;
        border-bottom: 1px solid darkseagreen;
        padding: 5px;
    }
    .commentedDate{
        font-size: 13px;
    }
    .comment{
        margin: 0px 20px;
        padding: 5px 5px 8px 5px;
        font-size: 14px;
    }
    #deleteSubmitBtn{
        border: 1.5px solid darkseagreen;
        padding: 4px 13px;
        color: dimgray;
        margin: 0px;
    }
    #deleteDisMiss{
        background-color: darkseagreen;
        padding: 4px 13px;
        color: white;
        margin: 0px;
        margin-right: 20px;
    }
    #deleteModalFooter{
        padding-right: 2em;
    }
    #provePassword{
        display: block;
        top: 25%;
    }
    #proveForm{
        margin-block-start: 1em;
        margin-left: 1em;
        font-size: 15px;
    }
    #proveLabel{
        margin-right: 1em;
    }
    #insertToProve{
        border-bottom: 1px solid darkseagreen;
        width: 60%;
    }
    #deleteModalContent{
        width: 85%;
    }
</style>
    <h4 style="margin: 0px;margin-bottom: 2em;">게시판 - 보기</h4>
    <div>
        <div>
            <div id="categoryAndTitle">
                <div id="categoryNameDiv">[<%=(request.getAttribute("categoryName"))%>]</div>
                <div id="titleDiv"><%=request.getAttribute("title")%></div>
            </div>
            <span id="elseDetailsSpan">
                <span id="nicknameSpan"><%=request.getAttribute("nickname")%></span>
                <span id="elseDetails">
                    <span>조회수: <%=request.getAttribute("viewCount")%></span>
                    <span>등록일시: <%=request.getAttribute("submitDate")%></span>
                    <span>수정일시: <%=request.getAttribute("updateDate") == null? "" : request.getAttribute("updateDate")%></span>
                </span>
            </span>
        </div>
        <div id="contentText">
            <%=request.getAttribute("content")%>
        </div>
    </div>
    <div id="CommentBlock">
        <div>
            <form id="commentForm" action="commentProcess.jsp" method="get">
                <input type="hidden" name="commentedContentId" value='<%=request.getAttribute("contentId")%>'/>
                <span id="nicknameAndSave">
                    <label style="font-size: 13px;">
                        <input id="nicknameInput" placeholder="닉네임" name="commentNickname" type="text"/>
                    </label>
                    <input id="saveBtn" type="submit" value="등록"/>
                </span>
                <label id="textareaLabel">
                    <textarea id="textareaContent" name="commentText" type="text" minlength="1" placeholder="댓글을 입력해 주세요"></textarea>
                </label>
            </form>
        </div>
        <c:if test='<%=request.getAttribute("comments") != null%>'>
            <c:forEach var="comments" items='<%=(List<CommentsEntity>) request.getAttribute("comments")%>'>
                <div class="commentBlock">
                    <span class="userAndDate">
                        <span class="commentUser">${comments.commentUser}</span>
                        <span class="commentedDate">${comments.commentedDate}</span>
                    </span>
                    <span class="comment">${comments.comment}</span>
                </div>
            </c:forEach>
        </c:if>
    </div>
    <div id="deleteModal"></div>
    <span id="buttons">
        <button type="button" class="btn" onclick="moveToBoard()">목록</button>
        <button type="button" class="btn" onclick="goToAdjust()">수정</button>
        <button type="button" class="btn" onclick="callDeleteModal()">삭제</button>
    </span>