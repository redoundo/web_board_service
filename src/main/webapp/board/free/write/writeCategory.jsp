<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<c:set var="savedCategories" value='<%=request.getAttribute("categories")%>'/>
<option value=null>카테고리 선택</option>
<c:forEach var="categoryId" items="${savedCategories.keySet().toArray()}">
    <option value="${Integer.parseInt(categoryId.toString())}">${savedCategories.get(categoryId).toString()}</option>
</c:forEach>