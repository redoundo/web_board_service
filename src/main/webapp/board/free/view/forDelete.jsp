<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="modal show" id="provePassword" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="proveForm" aria-hidden="false">
    <div class="modal-dialog">
        <div id="deleteModalContent" class="modal-content">
            <div class="modal-header">게시글 삭제</div>
            <div class="modal-body">
                <form id="proveForm">
                    <input type="hidden" name="contentId" value='<%=request.getAttribute("contentId")%>'/>
                    <label id="proveLabel" for="insertToProve">비밀번호*</label>
                    <input id="insertToProve" name="password" placeholder="비밀번호를 입력해 주세요." required/>
                </form>
            </div>
            <div id="deleteModalFooter" class="modal-footer">
                <button onclick="displayNone()" id="deleteDisMiss" data-bs-dismiss="modal" type="button">취소</button>
                <input id="deleteSubmitBtn" type="submit" formaction='<%=request.getRequestURL().toString().replace("http://localhost:8080" , "").replace("modalProcess.jsp" , "deleteProcess.jsp").replace("view.jsp" , "deleteProcess.jsp")%>'
                 formmethod="post" form="proveForm" value="확인"/>
            </div>
        </div>
    </div>
</div>