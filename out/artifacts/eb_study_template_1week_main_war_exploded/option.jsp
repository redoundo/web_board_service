<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2024-01-14
  Time: 오후 3:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.Date" %>
<%@page import="java.time.ZoneId"%>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.ResultSet" %>
<!DOCTYPE html>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%!Connection con;
        {
            try {
                con = new ConnectionTest().getConnection();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    %>
    <div id="BoardOptDiv">
        <form id="OptForm" method="get" name="form" action="http://localhost:8080/SearchOpt">
            <span id="OptFormSpan">
                <p id="RegistrationDateText">
                    등록일
                </p>
                <span id="SetRegisteredDate">
                    <c:set var="today" value="<%=new java.util.Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()%>" scope="application"/>
                    <fmt:formatDate value="${today}"/>
                    <label for="RegisteredDateFrom">
                        <input name="start" id="RegisteredDateFrom" type="date" value="${today.minusYears(1)}"/>
                    </label>
                    <p>~</p>
                    <label for="RegisteredDateEnd">
                        <input name="end" id="RegisteredDateEnd" type="date" value="${today}"/>
                    </label>
                </span>
                <span id="SearchOpt">
                    <%!List<String> category;%>
                    <%
                        try {
                            ResultSet rs=con.createStatement()
                                    .executeQuery("SELECT DISTINCT CATEGORY FROM contents;");
                            while(rs.next()){
                                category.add(rs.getString("CATEGORY"));
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    %>
                    <label for="CategoryOpt">
                        <select id="CategoryOpt" name="category">
                            <option value=0 selected>전체 카테고리</option>
                            <c:forEach var="categories" varStatus="opt" items="<%=category%>">
                                <option value="${opt.index+1}">${categories}</option>
                            </c:forEach>
                        </select>
                    </label>
                    <label for="KeywordInput">
                        <input name="keyword" id="KeywordInput" placeholder="검색어를 입력해주세요.(제목+작성자+내용)"/>
                    </label>
                    <input type="submit"  value="검색"/>
                </span>
            </span>
        </form>
    </div>
</body>
</html>
