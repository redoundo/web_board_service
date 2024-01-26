<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.study.connection.dto.ContentDto" %>
<%@ page import="com.study.connection.dao.ContentDao" %>
<%@ page import="com.study.connection.dao.CategoriesDao" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html lang="ko">
<head>
    <title>게시판 - 수정</title>
    <meta charset="UTF-8">
</head>
<body>
<%
    request.setCharacterEncoding("UTF-8");
    Map<String , String> map =new HashMap<>();
    String contentId = request.getParameter("content_id");
    map.put("content_id" , contentId);

    ContentDao dao = new ContentDao();
    ContentDto dto = dao.select(map).get(0);

    Map<Integer , String> categories = new CategoriesDao().select();
%>
<div id="mgt_course_modify">
    <h2>게시판 - 수정</h2>
    <h5>기본정보</h5>
    <div class="OptForm">
        <form id="ModifyForm" enctype="multipart/form-data">
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
                    <td><%=categories.get(dto.getContentCategoryId())%>
                    </td>
                </tr>
                <tr>
                    <th scope="row">등록 일시</th>
                    <td><%=dto.getSubmitDate()%>
                    </td>

                </tr>
                <tr>
                    <th scope="row">수정 일시</th>
                    <td><%=dto.getUpdateDate() != null ? dto.getUpdateDate() : "-"%>
                    </td>
                </tr>
                <tr>
                    <th scope="row">작성자*</th>
                    <td>
                        <label for="Writer">
                            <input value="<%=dto.getNickname()%>" name="modifyWriter" id="Writer"
                                   type="text" maxlength="4" minlength="3" required/>
                        </label>
                    </td>
                </tr>
                <tr>
                    <th scope="row">비밀번호 확인*</th>
                    <td>
                        <label>
                            <input name="contentId" type="hidden" value='<%=request.getAttribute("content_id")%>'/>
                            <input id="originalPassword" type="hidden" value="<%=dto.getPassword()%>"/>
                            <input id="proveForModify" placeholder="비밀번호" type="password"
                                   required maxlength="15" minlength="4"/>
                        </label>
                    </td>
                </tr>
                <tr>
                    <th scope="row">제목*</th>
                    <td>
                        <label for="writeTitle">
                            <input value="<%=dto.getTitle()%>" name="modifyTitle" type="text"
                                   id="writeTitle" required maxlength="99" minlength="4"/>
                        </label>
                    </td>
                </tr>
                <tr>
                    <th scope="row">내용*</th>
                    <td>
                        <label for="writeContent">
                            <input value="<%=dto.getContent()%>" name="modifyContent" type="text"
                                   maxlength="1999" minlength="4" id="writeContent" required/>
                        </label>
                    </td>
                </tr>
                <tr>
                    <th scope="row">파일 첨부</th>
                    <td>
                        <c:choose>
                            <c:when test='<%=request.getAttribute("file_existence") != null%>'>
                                <c:set var="files"
                                       value='<%=(List<Map<String,String>>) request.getAttribute("files")%>'/>
                                <c:if test="${files.size() == 1}">
                                    <a download="${files.get(0).get("file_path")}">${files.get(0).get("file_name")}</a>
                                    <label><input name="modify_file2" type="file"/></label>
                                    <label><input name="modify_file3" type="file"/></label>
                                </c:if>
                                <c:if test="${ files.size() == 2 }">
                                    <a download="${files.get(0).get("file_path")}">${files.get(0).get("file_name")}</a>
                                    <a download="${files.get(1).get("file_path")}">${files.get(1).get("file_name")}</a>
                                    <label><input name="modify_file3" type="file"/></label>
                                </c:if>
                                <c:if test="${ files.size() == 3 }">
                                    <a download="${files.get(0).get("file_path")}">${files.get(0).get("file_name")}</a>
                                    <a download="${files.get(1).get("file_path")}">${files.get(1).get("file_name")}</a>
                                    <a download="${files.get(2).get("file_path")}">${files.get(2).get("file_name")}</a>
                                </c:if>
                            </c:when>
                            <c:otherwise>
                                <label for="addFile1"><input name="modify_file1" type="file" id="addFile1"/></label>
                                <label for="addFile2"><input name="modify_file2" type="file" id="addFile2"/></label>
                                <label for="addFile3"><input name="modify_file3" type="file" id="addFile3"/></label>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                </tbody>
            </table>
            <%--MainServlet 에서 command 구별에 필요한 Type 내용--%>
            <input type="hidden" name="Type" value="MODIFY_UPDATE"/>
        </form>
        <div>
            <button type="button" onclick="toView()">취소</button>
            <div>
                <input class="input apply" onsubmit="passwordAjax()" form="ModifyForm" type="submit"
                       formaction="main" formmethod="post" value="저장" formenctype="multipart/form-data"/>
            </div>
        </div>
    </div>
</div>
    <%--modify.jsp 끝--%>

<script type="text/javascript">
    //view.jsp로 이동한다. view.jsp 에서 왔으므로 필요한 내용은 쿼리스트링에 존재할 것이다.
    function toView() {
        return location.href = window.location.href.replace("http://localhost:8080", "").replace("modify/modify.jsp", "view/view.jsp");
    }

    function passwordAjax() {
        let provePassword = $("#proveForModify").attr("value");
        const originalPassword = $("#originalPassword").attr("value"); //db에서 가져온 password는 이미 단방향 암호화가 되어져 있다.
        //submit 여부를 통제하는 변수
        //TODO : onSubmit에서 false가 반환되면 submit이 되지 않는지 여부 확인이 필요하다.
        let valid = false;
        $.ajax({
            type: "POST", url: window.location.href.replace("http://localhost:8080", "")
                .replace("modify.jsp", "encryptChecking.jsp"),
            data: {forProve: provePassword},
            success(data) {
                if (data !== originalPassword) {
                    alert("정확한 비밀번호를 입력해주세요.")
                } else {
                    valid = true;
                }
            },
            error(error) {
                console.log(error);
                alert("잠시후 다시 시도해주세요.")
            }
        })
        return valid;
    }
</script>
</body>
</html>
