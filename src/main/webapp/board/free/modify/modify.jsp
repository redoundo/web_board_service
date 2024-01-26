<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8" %>
<html lang="ko">
<head>
    <title>게시판 - 수정</title>
</head>
<body>
<script type="text/javascript">
    function adjustAjax() {
        $.ajax({
            type : "GET" , url : window.location.href.replace("http://localhost:8080" , "").replace("modify.jsp" , "modifyProcess.jsp")
        })
        $(this).off();
    }
    $(adjustAjax);
</script>
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
                    <td><%=request.getAttribute("category_name")%></td>
                </tr>
                <tr>
                    <th scope="row">등록 일시</th>
                    <td><%=request.getAttribute("submit_date")%>
                    </td>

                </tr>
                <tr>
                    <th scope="row">수정 일시</th>
                    <td><%=request.getAttribute("update_date")%>
                    </td>
                </tr>
                <tr>
                    <th scope="row">작성자*</th>
                    <td>
                        <label for="Writer">
                            <input value="<%=request.getAttribute("nickname")%>" name="modifyWriter" id="Writer"
                                   type="text" maxlength="4" minlength="3" required/>
                        </label>
                    </td>
                </tr>
                <tr>
                    <th scope="row">비밀번호 확인*</th>
                    <td>
                        <label>
                            <input name="contentId" type="hidden" value="<%=request.getAttribute("content_id")%>"/>
                            <input id="originalPassword" type="hidden" value="<%=request.getAttribute("password")%>"/>
                            <input id="proveForModify" placeholder="비밀번호" type="password"
                                   required maxlength="15" minlength="4"/>
                        </label>
                    </td>
                </tr>
                <tr>
                    <th scope="row">제목*</th>
                    <td>
                        <label for="writeTitle">
                            <input value="<%=request.getAttribute("title")%>" name="modifyTitle" type="text"
                                   id="writeTitle" required maxlength="99" minlength="4"/>
                        </label>
                    </td>
                </tr>
                <tr>
                    <th scope="row">내용*</th>
                    <td>
                        <label for="writeContent">
                            <input value="<%=request.getAttribute("content")%>" name="modifyContent" type="text"
                                   maxlength="1999" minlength="4" id="writeContent" required/>
                        </label>
                    </td>
                </tr>
                <tr>
                    <th scope="row">파일 첨부</th>
                    <td>
                        <c:choose test='<%=request.getAttribute("file_existence") != null%>'>
                            <c:set var="files" value="<%=(List<Map<String,String>>) request.getAttribute("files")%>"/>
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
                        </c:choose>
                        <c:otherwise>
                            <label for="addFile1"><input name="modify_file1" type="file" id="addFile1"/></label>
                            <label for="addFile2"><input name="modify_file2" type="file" id="addFile2"/></label>
                            <label for="addFile3"><input name="modify_file3" type="file" id="addFile3"/></label>
                        </c:otherwise>
                    </td>
                </tr>
                </tbody>
            </table>
        </form>
        <div>
            <button type="button" onclick="toViewProcess()">취소</button>
            <div>
                <input class="input apply" onsubmit="passwordAjax()" form="ModifyForm" type="submit"
                       formaction="<%=request.getRequestURL().toString().replace("http://localhost:8080" , "")
                       .replace("modify.jsp" , "updateModify.jsp")%>"
                       formmethod="post" value="저장" formenctype="multipart/form-data"/>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    //viewProcess.jsp로 이동한다.
    function toViewProcess () {
        return location.href = window.location.href.replace("http://localhost:8080" , "").replace("modify/modify.jsp" , "view/viewProcess.jsp");
    }
    function passwordAjax () {
        let provePassword = $("#proveForModify").attr("value");
        const originalPassword = $("#originalPassword").attr("value"); //db에서 가져온 password는 이미 단방향 암호화가 되어져 있다.
        //submit 여부를 통제하는 변수
        //TODO : onSubmit에서 false가 반환되면 submit이 되지 않는지 여부 확인이 필요하다.
        let valid = false;
        $.ajax({
            type : "POST" , url : window.location.href.replace("http://localhost:8080" , "")
                .replace("modify.jsp" , "encryptChecking.jsp") ,
            data : { forProve : provePassword },
            success (data) {
                if(data !== originalPassword) {
                    alert("정확한 비밀번호를 입력해주세요.")
                }else {
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
