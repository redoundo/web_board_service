<%@ page import="java.util.List" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.study.connection.dao.ContentDao" %>
<%@ page import="com.study.connection.dto.ContentDto" %>
<%@ page import="com.study.connection.dao.CategoriesDao" %>
<%@ page import="java.sql.Date" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html lang="ko">
<head>
    <title>자유 게시판 - 목록입니다.</title>
    <meta charset="UTF-8">
</head>
<body>
<%

    request.setCharacterEncoding("UTF-8");
    //검색했을 경우를 대비하여 검색 조건이 쿼리스트링에 존재하는지 확인.
    String end = request.getParameter("end");
    String start = request.getParameter("start");
    String category = request.getParameter("category");
    String keyword = request.getParameter("keyword");
    String content= request.getParameter("content_id");
    String pageNumber = request.getParameter("page");

    Map<String , String> map = new HashMap<>();
    map.put("end" , end);
    map.put("start" , start);
    map.put("content_category_id" , category);
    map.put("content_id" , content);
    map.put("keyword" , keyword);
    map.put("page" , pageNumber);//limit page,10. default 1.

    ContentDao dao= new ContentDao();
    List<ContentDto> dtos = dao.select(map);//최대 10개만 존재.
    Integer total = dao.total();//위 조건의 전체 값. default 0.

    Map<Integer , String> categories = new CategoriesDao().select();

    Date today = Date.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("y-M-d")));
    Date before = Date.valueOf(LocalDateTime.now().minusYears(1).format(DateTimeFormatter.ofPattern("y-M-d")));
%>
<div id="Board">
    <h2>자유 게시판 - 목록입니다.</h2>
    <div>
        <div id="BoardOptDiv">
            <form id="OptForm" method="get" action="main">
            <div id="OptFormSpan">
                <p id="RegistrationDateText"> 등록일</p>
                <span id="SetRegisteredDate">
                    <label for="RegisteredDateFrom">
                        <input name="start" id="RegisteredDateFrom" type="date" value='<%=
                        request.getParameter("start") != null ? Date.valueOf(request.getParameter("start")) : before%>'/>
                    </label>
                    <p>~</p>
                    <label for="RegisteredDateEnd">
                        <input name="end" id="RegisteredDateEnd" type="date" value='<%=
                        request.getParameter("end") != null ? Date.valueOf(request.getParameter("end")) : today%>'/>
                    </label>
                </span>
                <span id="SearchOpt" >
                    <label for="CategoryOpt">
                        <select id="CategoryOpt" name="category" >
                            <c:set var="categorMap" value='<%=categories%>'/>
                            <c:choose>
                                <c:when test='<%=request.getParameter("category") != null &&
                                !request.getParameter("category").isEmpty() &&
                                !request.getParameter("category").equals("null")%>'>
                                    <option value=null>전체 카테고리</option>
                                    <c:set var="req" value='<%=request.getParameter("category")%>'/>
                                    <c:forEach var="names" items="${categorMap.keySet().toArray()}">
                                        <c:if test="${names == req}">
                                            <option value="${names}" selected>${categorMap.get(names)}</option>
                                        </c:if>
                                        <option value="${names}">${categorMap.get(names)}</option>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <c:if test="<%=categories != null && categories.size() >1%>">
                                        <option value=null selected>전체 카테고리</option>
                                        <c:forEach var="name" varStatus="opt" items="${categorMap.keySet().toArray()}">
                                            <option value="${name}">${categorMap.get(name)}</option>
                                        </c:forEach>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                        </select>
                    </label>
                    <label for="KeywordInput">
                        <input name="keyword" id="KeywordInput" placeholder="검색어를 입력해주세요.(제목+작성자+내용)"/>
                    </label>
                </span>
            </div>
                <input type="hidden" name="Type" value="BOARD_SELECT"/>
                <input type="submit" value="검색" />
            </form>
        </div>
    </div>
    <div id='Result'>
        <h6 id='ResultsCount'>총 <%=total%>건</h6>
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
                <c:when test='<%=total > 0 && !dtos.isEmpty()%>'>
                    <tbody>
                    <c:forEach var="result" varStatus="res" items='<%=dtos%>'>
                        <th scope="row"></th>
                        <tr>${categorMap.get(result.contentCategoryId)}</tr>
                        <tr>${result.fileExistence == true? "존재" : ""}</tr>
                        <%--content_id를 param으로 가진 채 각각의 content로 이동하는 input 버튼--%>
                        <input class="moveInput" type="button" value="${result.title}" onclick="moveToView(${result.contentId})"/>
                        <tr>${result.nickname}</tr>
                        <tr>${result.viewCount}</tr>
                        <tr>${result.submitDate}</tr>
                        <tr>${result.updateDate == null? "-" : result.updateDate}</tr>
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
    <%@include file="pagination.jsp"%>
</div>
<div id="creatContentDiv">
    <button type="button" onclick="location.href='/board/free/write/write.jsp'">등록</button>
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
</body>
</html>