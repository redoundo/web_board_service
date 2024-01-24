<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%!Map<Integer,Object> PagedResult(){
    Map<Integer,Object> divided_by_100=new HashMap<>();
    if(SearchResult!=null){
        int totalResult=SearchResult.size();
        if(totalResult>10){
            float divided=(float) totalResult/10;
            int ceil=(int)Math.ceil(divided);
            if(totalResult>100){
                int over_100=(int)Math.ceil(divided/10);
                for(int b=1;b<=over_100;b++){
                    Map<Integer,Object> divide_by_10=new HashMap<>();
                    divide_by_10.put((b-1)*10,SearchResult.subList((b-1)*10,b*10));
                    divided_by_100.put(b,divide_by_10);
                }
            }else{
                Map<Integer,Object> rr=new HashMap<>();
                for(int a=1;a<=ceil;a++){
                    rr.put(a,SearchResult.subList((a-1)*10,a*10));
                }
                divided_by_100.put(1,rr);
            }
        }else{divided_by_100.put(1,SearchResult.subList(0,SearchResult.size()));}
    }else{
        divided_by_100=null;
    }
    return divided_by_100;
}%>
<%!Map<Integer,Object>Paged=PagedResult();%>
<%!int page_by_100=Paged!=null?Paged.size():0;%>
<%!int now_100_page=1;%>
<%!int now_10_page=1;%>
<div id="pagination">
    <%!void MovePage(int by,int tf){
        if(by==100){
            now_100_page=now_100_page+tf;
        }else{
            now_10_page=now_10_page+tf;
        }
    }%>
    <c:choose>
        <c:when test="<%=page_by_100!=0&&now_100_page!=1%>">
            <button  type="submit" formmethod="get" formaction="pagination.jsp" value="<%=now_100_page-1%>">
                <i class="bi bi-chevron-double-left"></i>
            </button>
        </c:when>
        <c:otherwise>
            <button type="submit" formmethod="get" formaction="pagination.jsp" value="<%=now_100_page-1%>" disabled>
                <i class="bi bi-chevron-double-left"></i>
            </button>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="<%=page_by_100!=0&&now_10_page!=1%>">
            <button  type="submit" formmethod="get" formaction="pagination.jsp" value="<%=now_10_page-1%>">
                <i class="bi bi-chevron-left"></i>
            </button>
        </c:when>
        <c:otherwise>
            <button disabled type="submit" formmethod="get" formaction="pagination.jsp" value="<%=now_10_page-1%>">
                <i class="bi bi-chevron-left"></i>
            </button>
        </c:otherwise>
    </c:choose>

    <c:choose>
        <c:when test="<%=page_by_100!=0&&now_10_page!=1%>">
            <button  type="submit" formmethod="get" formaction="pagination.jsp" value="<%=now_10_page+1%>">
                <i class="bi bi-chevron-right"></i>
            </button>
        </c:when>
        <c:otherwise>
            <button disabled type="submit" formmethod="get" formaction="pagination.jsp" value="<%=now_10_page+1%>">
                <i class="bi bi-chevron-right"></i>
            </button>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="<%=page_by_100!=0&&now_100_page<page_by_100%>">
            <button  type="submit" formaction="pagination.jsp" formmethod="get" value="<%=now_100_page+1%>">
                <i class="bi bi-chevron-double-right"></i>
            </button>
        </c:when>
        <c:otherwise>
            <button disabled type="submit" formaction="pagination.jsp" formmethod="get" value="<%=now_100_page+1%>">
                <i class="bi bi-chevron-double-right"></i>
            </button>
        </c:otherwise>
    </c:choose>
</div>
