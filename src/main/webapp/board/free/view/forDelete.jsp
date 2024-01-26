<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="modal fade" id="provePassword" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="proveForm" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header"></div>
            <div class="modal-body">
                <form id="proveForm">
                    <label id="proveLabel" for="insertToProve">비밀번호*</label>
                    <input id="insertToProve" name="password" placeholder="비밀번호를 입력해 주세요." required/>
                </form>
            </div>
            <div class="modal-footer">
                <button data-bs-dismiss="modal" type="button">취소</button>
                <input type="submit" formaction="View.jsp" formmethod="post" form="proveForm" value="확인"/>
            </div>
        </div>
    </div>
</div>