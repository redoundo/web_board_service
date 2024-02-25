<%@ page import="java.util.List" %>
<%@ page import="com.study.connection.entity.ContentsEntity" %>
<%@ page import="java.time.ZoneId" %>
<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="Board">
    <h2>자유 게시판 - 목록입니다.</h2>
    <div>
        <div id="BoardOptDiv">
            <form id="OptForm">
            <span id="OptFormSpan">
                <p id="RegistrationDateText">
                    등록일
                </p>
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
                <span id="SearchOpt" >
                    <label for="CategoryOpt">
                        <select id="CategoryOpt" name="category" >
                            <c:set var="categorMap" value='<%=request.getAttribute("categories")%>'/>
                            <option value="" selected>전체 카테고리</option>
                            <c:forEach var="categories" varStatus="opt" items="${categorMap.values().toArray()}">
                                <option value="${opt.index+1}">${categories}</option>
                            </c:forEach>
                        </select>
                    </label>
                    <label for="KeywordInput">
                        <input name="keyword" id="KeywordInput" placeholder="검색어를 입력해주세요.(제목+작성자+내용)"/>
                    </label>
                </span>
            </span>
            </form>
            <input form="OptForm" type="submit" value="검색" formmethod="get" formaction="boardProcess.jsp"/>
        </div>
    </div>
    <div id='Result'>
        <h6 id='ResultsCount'>총 <%=request.getAttribute("total").getClass().getName().equals("java.lang.Integer") ?  request.getAttribute("total") : 0%>건</h6>
        <table id='ResultList'>
            <thead>
            <tr>
                <th scope="col"></th>
                <th scope="col">카테고리</th>
                <th scope="col"></th>
                <th scope="col">제목</th>
                <th scope="col">작성자</th>
                <th scope="col">조회수</th>
                <th scope="col">등록일시</th>
                <th scope="col">수정일시</th>
            </tr>
            </thead>
            <c:choose>
                <c:when test='<%=request.getAttribute("total") != null || (Integer) request.getAttribute("total") != 0%>'>
                    <tbody>
                    <c:forEach var="initresults" varStatus="res" items='<%=(List<ContentsEntity>) request.getAttribute("contents")%>'>
                        <th scope="row"></th>
                        <tr>${categorMap.get(initresults.contentCategoryId)}</tr>
                        <tr>${initresults.fileExistence == true? "존재" : ""}</tr>
                        <%--content_id를 param으로 가진 채 각각의 content로 이동하는 input 버튼--%>
                        <input class="moveInput" type="button" value="${initresults.title}" onclick="moveToView(${initresults.contentId})"/>
                        <tr>${initresults.nickname}</tr>
                        <tr>${initresults.viewCount}</tr>
                        <tr>${initresults.submitDate}</tr>
                        <tr>${initresults.updateDate == null? "-" : initresults.updateDate}</tr>
                    </c:forEach>
                    </tbody>
                </c:when>
                <c:otherwise>
                    <div id="noResultsExist">
                        결과가 존재하지 않습니다.
                    </div>
                </c:otherwise>
            </c:choose>
        </table>
    </div>
</div>
<div id="creatContentDiv">
    <button type="button" onclick="location.href='board/free/write/write.jsp'">등록</button>
</div>
<script type="text/javascript">
    function moveToView(contentId){
        if (window.location.href.includes("content_id=") === false) {
            if(window.location.href.includes(".jsp?")) {
                //?을 포함하면 검색을 한걸로 취급하고 &content_id를 붙인다.
                return location.href = window.location.href.replace("http://localhost:8080" , "").replace("list/board.jsp" , "view/view.jsp") + "&content_id=" + contentId;
            }else{
                //아니면 ?을 붙이고 content_id를 붙인다.
                return location.href = window.location.href.replace("http://localhost:8080" , "").replace("list/board.jsp" , "view/view.jsp") + "?content_id=" +contentId;
            }
        }
    }
</script>