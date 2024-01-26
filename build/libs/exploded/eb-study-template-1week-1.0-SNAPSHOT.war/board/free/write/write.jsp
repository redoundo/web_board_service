<%@ page import="com.study.connection.dao.CategoriesDao" %>
<%@ page import="java.util.Map" %>
<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>게시판 - 등록</title>
</head>
<body>
<%
    request.setCharacterEncoding("UTF-8");
    Map<Integer , String> categories = new CategoriesDao().select();

%>
<div id="writing">
    <h2>게시판 - 등록</h2>
    <div>
        <div class="OptForm">
            <form id="OptionForm">
                <table>
                    <thead>
                    <tr style="display: none">
                        <th scope="col"></th>
                        <th scope="col"></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <th scope="row">카테고리*</th>
                        <td>
                            <label for="categorySelect">
                                <select id="categorySelect" name="writeCategory">
                                    <c:if test="<%=categories != null && !categories.isEmpty() %>">
                                        <c:set var="categories" value="<%=categories%>"/>
                                        <c:forEach var="category" items="<%=categories.keySet().toArray()%>">
                                            <option value="${category}">${categories.get(category)}</option>
                                        </c:forEach>
                                    </c:if>
                                </select>
                            </label>
                        </td>
                    </tr>
                    <tr>
                        <th scope="row">작성자*</th>
                        <td>
                            <label for="Writer">
                                <input name="writeNickname" id="Writer" type="text" maxlength="4" minlength="3" required/>
                            </label>
                        </td>
                    </tr>
                    <tr>
                        <th scope="row">비밀번호*</th>
                        <td>
                            <label>
                                <input name="password" placeholder="비밀번호" type="password" required maxlength="15" minlength="4"/>
                            </label>
                            <label for="reProve">
                                <input name="provePassword" id="reProve" placeholder="비밀번호 확인" type="password" required maxlength="15" minlength="4"/>
                            </label>
                        </td>
                    </tr>
                    <tr>
                        <th scope="row">제목*</th>
                        <td>
                            <label for="writeTitle">
                                <input name="writeTitle" type="text" id="writeTitle" required maxlength="99" minlength="4"/>
                            </label>
                        </td>
                    </tr>
                    <tr>
                        <th scope="row">내용*</th>
                        <td>
                            <label for="writeContent">
                                <input name="writeContent" type="text" maxlength="1999" minlength="4" id="writeContent"/>
                            </label>
                        </td>
                    </tr>
                    <tr>
                        <th scope="row">파일 첨부</th>
                        <td>
                            <label for="addFile1"><input name="writeFile1" type="file" id="addFile1"/></label>
                            <label for="addFile2"><input name="writeFile2" type="file" id="addFile2"/></label>
                            <label for="addFile3"><input name="writeFile3" type="file" id="addFile3"/></label>
                        </td>
                    </tr>
                    </tbody>
                </table>
                <%--MainServlet에서 command 제공 타입 : WRITE_INSERT--%>
                <input type="hidden" name="Type" value="WRITE_INSERT"/>
            </form>
            <div>
                <input form="OptionForm" type="submit"  formaction="main" formmethod="post" value="저장"/>
            </div>
            <div>
                <button type="button" onclick="cancelWrite()">취소</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    //write.jsp -> board.jsp 로 이동
    function cancelWrite() {
        return location.href = window.location.href.replace("http://localhost:8080" , "").replace("write/write.jsp" ,"list/board.jsp")
    }
</script>
</body>
</html>