<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="savedCategories" value='<%=request.getAttribute("categories")%>'/>
<option value=null>카테고리 선택</option>
<c:forEach var="categories" varStatus="index" items="${savedCategories.values().toArray()}">
    <option value="${index}">${savedCategories.get(categories)}</option>
</c:forEach>