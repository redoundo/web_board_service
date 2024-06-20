<%@ page import="java.util.List" %>
<%@ page import="com.study.connection.entity.ContentsEntity" %>
<%@ page import="java.time.ZoneId" %>
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<div id="Board">
    <style>
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
         #ResultCount{
            font-size: 13px;
            font-weight: 500;
            padding: 5px 0px;
            margin: 0px;
         }
         .category {
            flex-basis: 10%;
             text-align: center;
         }
         .fileExist {
            flex-basis: 5%;
             text-align: center;
         }
         .title {
            flex-basis: 50%;
             text-align: center;
         }
         .user {
             flex-basis: 10%;
             text-align: center;
         }
         .registerStart, .registerEnd {
            flex-basis: 10%;
             text-align: center;
         }
         .viewCount {
            flex-basis: 5%;
            text-align: center;
         }
         #OptForm {
            flex-basis: 94%;
            font-size: 14.5px;
         }
         #OptFormDiv {
            display: flex;
            flex-direction: row;
            gap: 1em;
         }
         #RegistrationDateText{
            font-size: 14px;
            line-height: 24px;
         }
         #BoardOptDiv{
            display: flex;
            flex-direction: row;
            justify-content: space-between;
            gap: 10px;
         }
         #SetRegisteredDate{
            display: flex;
            flex-direction: row;
            gap: 10px;
         }
         #SearchOpt{
            display: flex;
            flex-direction: row;
            gap: 14px;
            flex-grow: 1;
         }
         #forKeywordInput{
            flex-grow: 1;
            display: flex;
         }
         #OptFormSearch{
            flex-basis: 6%;
            font-size: 14.5px;
            background-color: darkseagreen;
            border: 1px solid transparent;
            color: white;
         }
         #keywordInput{
            padding: 4px 6px;
            flex-grow: 1;
         }
         #Result {
            display: flex;
            flex-direction: column;
            gap: 1em;
            margin: 15px 0px;
         }
         #ResultsCount{
            font-size: 13px;
            margin: 0px;
            padding: 5px 0px;
         }
         #ResultTableDiv{
            display: flex;
            flex-direction: column;
            gap: 10px;
         }
         #ResultTableHeader {
            display: flex;
            flex-direction: row;
            border-bottom: 2px solid darkseagreen;
            padding-bottom: 5px;
            font-size: 14px;
         }
         #ResultTableRows{
            display: flex;
            flex-direction: row;
            font-size: 14px;
            border-bottom: 1px solid darkseagreen;
            padding-bottom: 5px;
         }
         #WriteNewContent{
            padding: 5px 15px;
            font-size: 14px;
            background-color: darkseagreen;
            color: white;
            border: 1px solid transparent;
         }
         #createContentDiv{
            text-align: end;
            margin-top: 1em;
         }
    </style>
    <h4 style="margin: 0px;margin-bottom: 2em;">자유 게시판 - 목록입니다.</h4>
    <div>
        <script type="text/javascript">
            function moveToView(contentId){
                let href = window.location.href;
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
        </script>
        <div id="BoardOptDiv">
            <form id="OptForm">
                <div id="OptFormDiv">
                    <div id="RegistrationDateText">
                        등록일
                    </div>
                    <span id="SetRegisteredDate">
                        <c:set var="today" value="<%=new java.util.Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()%>" scope="application"/>
                        <label for="RegisteredDateFrom">
                            <input name="start" id="RegisteredDateFrom" type="date" value="${today.minusYears(1)}"/>
                        </label>
                        <p>~</p>
                        <label for="RegisteredDateEnd">
                            <input name="end" id="RegisteredDateEnd" type="date" value="${today}"/>
                        </label>
                    </span>
                    <span id="SearchOpt">
                        <label id="forCategoryOpt" for="CategoryOpt">
                            <select id="CategoryOpt" name="category" >
                                <c:set var="categoryMap" value='<%=request.getAttribute("categories")%>'/>
                                <option value="" selected>전체 카테고리</option>
                                <c:forEach var="categories" varStatus="opt" items="${categoryMap.values().toArray()}">
                                    <option value="${opt.index+1}">${categories}</option>
                                </c:forEach>
                            </select>
                        </label>
                        <label id="forKeywordInput" for="KeywordInput" >
                            <input name="keyword" id="KeywordInput" placeholder="검색어를 입력해주세요.(제목+작성자+내용)"/>
                        </label>
                    </span>
                </div>
            </form>
            <input form="OptForm" type="submit" id="OptFormSearch" value="검색" formmethod="get" formaction="boardProcess.jsp"/>
        </div>
    </div>
    <div id='Result'>
        <h6 id='ResultsCount'>
            총 <%=request.getAttribute("total").getClass().getName().equals("java.lang.Integer") ?  request.getAttribute("total") : 0%>건
        </h6>
        <div id="ResultTableDiv">
            <div id="ResultTableHeader">
                <span class="category">카테고리</span>
                <span class="fileExist"></span>
                <span class="title">제목</span>
                <span class="user">작성자</span>
                <span class="viewCount">조회수</span>
                <span class="registerStart">등록일시</span>
                <span class="registerEnd">수정일시</span>
            </div>
            <c:choose>
                <c:when test='<%=request.getAttribute("total") != null || (Integer) request.getAttribute("total") != 0%>'>
                    <c:forEach var="initresults" varStatus="res" items='<%=(List<ContentsEntity>) request.getAttribute("contents")%>'>
                        <div id="ResultTableRows">
                            <span class="category">[${categoryMap.get(initresults.contentCategoryId)}]</span>
                            <span class="fileExist">
                                <c:choose>
                                    <c:when test="${initresults.fileExistence == true}">
                                        <i class="bi bi-paperclip"></i>
                                    </c:when>
                                    <c:otherwise> </c:otherwise>
                                </c:choose>
                            </span>
                            <%--content_id를 param으로 가진 채 각각의 content로 이동하는 input 버튼--%>
                            <input class="moveInput title" type="button" value="${initresults.title}" onclick="moveToView(${initresults.contentId})"/>
                            <span class="user">${initresults.nickname}</span>
                            <span class="viewCount">${initresults.viewCount}</span>
                            <span class="registerStart">${initresults.submitDate}</span>
                            <span class="registerEnd">${initresults.updateDate == null? "-" : initresults.updateDate}</span>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div id="noResultsExist">
                        결과가 존재하지 않습니다.
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
<div id="createContentDiv">
    <button id="WriteNewContent" type="button" onclick="location.href='board/free/write/write.jsp'">
        등록
    </button>
</div>