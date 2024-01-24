<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <title>게시판 - 등록</title>
</head>
<body>
<script type="text/javascript">
    //로드될 때 한번만 실행되며 카테고리 id와 이름을 request에 담아오게 해주는 함수이다.
    function writeAjax() {
        $.ajax({
            type : "GET" ,
            url : window.location.href.replace("http://localhost:8080" , "").replace("write.jsp" , "writeProcess.jsp")
        })
        $(this).off();
    }
    $(writeAjax)
</script>
<div id="mgt_course_write">
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
                        <%--el태그에 저장해 놓지 않으면 형변환이 계속 필요하다. 하지만 스크립틀릿이 아닌 el표현식의 경우 명시적 형변환이 불가능하다.--%>
                        <c:set var="savedCategories" value="<%=(Map<Integer,String>) request.getAttribute("categories")%>"/>
                        <th scope="row">카테고리*</th>
                        <td>
                            <label for="categorySelect">
                                <select id="categorySelect" name="writeCategory">
                                    <option value=null>카테고리 선택</option>
                                    <%--savedCategories의 value만 가지고 반복한다.--%>
                                    <c:forEach var="categories" varStatus="index" items="${savedCategories.values().toArray()}">
                                        <option value="${index}">${savedCategories.get(categories)}</option>
                                    </c:forEach>
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
                            <%--TODO : 파일 첨부 기능 구현 필요--%>
                            <label for="addFile1"><input name="file1" type="file" id="addFile1"/></label>
                            <label for="addFile2"><input name="file2" type="file" id="addFile2"/></label>
                            <label for="addFile3"><input name="file3" type="file" id="addFile3"/></label>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </form>
            <div>
                <input form="OptionForm" type="submit"  formaction="/board/free/write/writeProcess.jsp" formmethod="post" value="저장"/>
            </div>
            <div>
                <button type="button" onclick="cancelWrite()">취소</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    function cancelWrite() {
        return location.href = "/board/free/list/board.jsp";
    }
</script>
</body>
</html>

