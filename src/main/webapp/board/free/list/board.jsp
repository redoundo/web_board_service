<%@ page import="java.util.List" %>
<%@ page import="com.study.connection.entity.ContentsEntity" %>
<%@ page import="java.time.ZoneId" %>
<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>자유 게시판 - 목록입니다.</title>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
</head>
<body>
<div id="Board">
    <h2>자유 게시판 - 목록입니다.</h2>
    <div>
        <%--초기 화면 출력에 필요한 db내용을 가져오기 위해 boardProcess.jsp로 ajax를 날린다.--%>
        <%--화면이 새로고침 되거나 최초로 출력된게 아니라면 반드시 한번만 실행되는 스크립트이다.--%>
        <%--ajax의 값은 받을 필요없다. setAttribute를 통해 페이지에서 불러낼 수 있다.--%>
        <script type="text/javascript">
            function ajax() {
                $.ajax({
                    type: "GET" , url: "/board/free/write/write.jsp?status=init"
                })
                $(this).off();
            }
            $(ajax);
        </script>
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
                            <c:set var="categorMap" value="<%=request.getAttribute("categories")%>"/>
                            <%--init이던 main이던 categories는 request의 결과값과 같이 온다.--%>
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
    <div id="Result">
        <h6 id="ResultsCount">총 <%=request.getParameter("total") == null? 0 : request.getParameter("total")%>건</h6>
        <table id="ResultList">
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
                <%--결과가 없거나 오류가 발생했을 수도 있으므로 조건문을 통해 확인한뒤 진입한다.--%>
                <c:when test="<%=request.getAttribute("total") != null || (Integer) request.getAttribute("total") != 0%>">
                    <tbody>
                    <c:forEach var="initresults" varStatus="res" items="<%=(List<ContentsEntity>) request.getAttribute("contents")%>">
                        <th scope="row"></th>
                        <tr>${categorMap.get(initresults.contentCategoryId)}</tr>
                        <tr>${initresults.fileExistence == 0? "존재" : ""}</tr>
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
        //TODO : 현재 경로가 어디로 될지 확실하게 정하지 못한 상태.
        //만약 검색을 했다면 boardProcess.jsp에서 쿼리스트링을 포함해줬을 것이다. 아니라면 오로지 content_id만 포함한다.
        if(window.location.href.includes(".jsp?")) {
            return location.href = window.location.href.replace("http://localhost:8080" , "").replace("list/board.jsp" , "view/view.jsp") + "&content_id=" + contentId;
        }else{
            return location.href = window.location.href.replace("http://localhost:8080" , "").replace("list/board.jsp" , "view/view.jsp") + "?content_id=" +contentId;
        }
    }
</script>
</body>
</html>
