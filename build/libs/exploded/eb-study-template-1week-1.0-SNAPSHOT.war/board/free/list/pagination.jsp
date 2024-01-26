<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<span id="pagination">
    <c:set var="total" value='<%= total %>'/>
    <c:set var="page" value='<%=request.getParameter("page") == null ? null : Integer.parseInt(request.getParameter("page"))%>'/>
    <c:choose>
        <%-- 값이 한페이지인 10보다는 많지만 10페이지인 100 보다는 적을 때 시작--%>
        <c:when test="${ total != null && total > 10 && total < 100}">
            <%--1페이지 이전 버튼 시작--%>
            <c:choose>
                <c:when test="${ page == null || page == 0}">
                    <%--아예 처음 || 이동한 페이지가 쿼리스트링 기준으로 0일 때--%>
                    <button type="button" class="Before onePage" disabled>1페이지 전</button>
                </c:when>
                <c:otherwise>
                    <button type="button" class="Before onePage" onclick="movePage(${page - 1})"> 1페이지 전 </button>
                </c:otherwise>
            </c:choose>
            <%--1페이지 이전 버튼 끝--%>
            <span>
                <%--페이지 숫자 버튼 시작--%>
                <c:forEach var="underHundred" items="${ total%10 != 0? total/10 : total/10 -1 }">
                    <button type="button" class="turnPage" onclick="movePage(${underHundred})"> ${ underHundred } </button>
                </c:forEach>
                <%--페이지 숫자 버튼 끝--%>
            </span>
            <span>
            <%--1페이지 뒤 버튼 시작--%>
            <c:choose>
                <c:when test="${ page == ( total%10 == 0 ? total/10 : total/10 -1 ) }">
                    <%--요청한 페이지가 (총 페이지 -1) 과 같을 때.--%>
                    <button type="button" class="After onePage" disabled>1페이지 뒤</button>
                </c:when>
                <c:otherwise>
                    <%--아예 처음 || 그외의 것들--%>
                    <button type="button" class="After onePage" onclick="movePage(${page + 1})">1페이지 뒤</button>
                </c:otherwise>
            </c:choose>
            <%--1페이지 뒤 버튼 끝--%>
            </span>
        </c:when>
        <%-- 값이 한페이지인 10보다는 많지만 10페이지인 100 보다는 적을 때 끝--%>
        <%--값이 100보다 많을 경우 시작--%>
        <c:when test="${ total != null && total != 0 && total >= 100 }">
            <%--페이지는 0부터 시작 page 쿼리스트링의 최소값은 실제로는 2페이지인 1.--%>
            <span>
                <%--10 페이지 이전 버튼 시작--%>
                <c:choose>
                    <c:when test='${ page == null || page <10 }'>
                        <%--페이지 이동을 한적이 없어서 page 쿼리 스트링이 존재하지 않을 때 ||
                    page 쿼리스트링이 9 이하여서 10페이지 전으로 가기가 불가능할때--%>
                        <button type="button" class="Before tenPage" disabled> 10페이지 전</button>
                    </c:when>
                    <c:otherwise>
                        <button type="button" class="Before tenPage" onclick="movePage(${page - 10})"> 10페이지 전 </button>
                    </c:otherwise>
                </c:choose>
                <%--10 페이지 이전 버튼 끝--%>
            </span>
            <span>
                <%--1페이지 이전 버튼 시작--%>
                <c:choose>
                    <c:when test='${ page == null || page == 0}'>
                       <%--아예 처음이거나,다른 페이지에서 맨 처음 페이지로 이동한 경우--%>
                        <button type="button" class="Before onePage" disabled> 1페이지 전 </button>
                    </c:when>
                    <c:otherwise>
                        <button type="button" class="Before onePage" onclick="movePage(${page - 1})"> 1페이지 전 </button>
                    </c:otherwise>
                </c:choose>
                <%--1페이지 이전 버튼 끝--%>
            </span>
            <span>
                <%--페이지 숫자 버튼 시작--%>
                <c:forEach var="overHundred"
                   begin="${page == null ? 1 : (page/10)*10+1}"
                   end="${page == null ? 10 : (page/10+1)*10}">
                    <%--page 쿼리스트링이 있다면 page 쿼리스트링을 기반으로 계산하여 구현.--%>
                    <button type="button" class="turnPage" onclick="movePage(${overHundred})"> ${ overHundred } </button>
                </c:forEach>
                <%--페이지 숫자 버튼 끝--%>
            </span>
            <span>
                <%--1페이지 뒤 버튼 시작--%>
                <c:choose>
                    <c:when test="${page == ( total%100 != 0 ? total/100 : total/100-1)}">
                        <%--맨 끝 페이지일 경우에 disabled--%>
                        <button type="button" class="After onePage" disabled> 1페이지 뒤 </button>
                    </c:when>
                    <c:otherwise>
                        <button type="button" class="After onePage" onclick="movePage(${page + 1})"> 1페이지 뒤 </button>
                    </c:otherwise>
                </c:choose>
                <%--1페이지 뒤 버튼 끝--%>
            </span>
            <span>
                <%--10페이지 뒤 버튼 시작--%>
                <c:choose>
                    <%--페이지가 (전체 페이지 -10)의 값 이상일 떄.--%>
                    <c:when test="${page >= (total%100 != 0? total/100 : (total/100-1) -10)}">
                        <button type="button" class="After tenPage" disabled> 10페이지 뒤 </button>
                    </c:when>
                    <c:otherwise>
                        <button type="button" class="After tenPage" onclick="movePage(${page + 10})"> 10페이지 뒤 </button>
                    </c:otherwise>
                </c:choose>
                <%--10 페이지 뒤 버튼 끝--%>
            </span>
        </c:when>
        <%--값이 100보다 많을 경우 끝--%>
        <%--total의 값이 null이거나 10개 이하일 때 시작--%>
        <c:otherwise>
            <span>
                <button type="button" class="turnPage" disabled>1</button>
            </span>
        </c:otherwise>
        <%--total의 값이 null이거나 10개 이하일 때 끝--%>
    </c:choose>
</span>
<script type="text/javascript">
    //페이지 이동용 함수
    function movePage (page) {
        return location.href = window.location.href.replace("page=[0-9]+" , "page=" + page).replace("http://localhost:8080" , "");
    }
</script>