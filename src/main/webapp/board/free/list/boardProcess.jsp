<%@ page import="java.util.Objects" %>
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.study.connection.entity.ContentsEntity" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.study.connection.CategoriesResults" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.study.connection.DBActions" %>
<%
    // 1. board.jsp 최초 화면 , 2. board.jsp 에서 검색하여 쿼리스트링을 포함한 상태일때 , 3. board.jsp -> write.jsp -> view.jsp -> 목록 버튼 -> board.jsp 경우로 경로가 쿼리스트링을 포함한 상태일 때.
    //TODO : 3번은 검색 후 글 작성 후 다시 검색되어져 있는 목록으로 돌아오기 위한 기능이다. 이런 기능이 있어야 하는건지 아니면 과한 것인지 물어보자.
    request.setCharacterEncoding("UTF-8");
    //최초 화면 여부를 알려주는 변수.
    String status = request.getParameter("status");
    System.out.println("status " + status);
    System.out.println("page " + request.getParameter("page"));
    List<String> conditionsList = new ArrayList<>();
    List<String> queryStringList = new ArrayList<>();
    String start = request.getParameter("start");
    String end = request.getParameter("end");
    String pageLimit = "";
    //현재 url에 있는 파라미터를 가지고 와 sql문을 생성시킨다.
    if (end != null || start != null) {
        if (end != null && start != null) {
            queryStringList.add("end=" + end);
            queryStringList.add("start=" + start);
            conditionsList.add(" submitDate BETWEEN " + "STR_TO_DATE('" + start + "','%Y-%m-%d') AND STR_TO_DATE('" + end + "','%Y-%m-%d')");
        } else {
            if(start == null) queryStringList.add("end=" + end);
            else queryStringList.add("start=" + start);
            conditionsList.add(" submitDate=STR_TO_DATE('" + start == null ? end : start + "','%Y.%m.%d') ");
        }
    } else {
        //1,2,3 모두 쿼리스트링에 특정 content_id가 있을 이유가 없다. 때문에 존재하더라도 추가하지 않는다.
        if (request.getParameter("contentId") == null) {

            for (String param : request.getParameterMap().keySet()) {
                String paramValue = request.getParameter(param);
                //화면 상태에 따라 함수 적용을 달리 할 예정이기에 따로 분기점을 만들었다.
                if(Objects.equals(param, "category")){
                    //태그 이름은 category이지만 실제로는 int인 content_category_id로 int는 '' 있으면 오류가 나기 때문에 따로 처리.
                    if (!paramValue.isEmpty()) {
                        //전체 카테고리는 빈 문자열이고 조건문으로 넣을 필요가 없다.
                        conditionsList.add(param + "=" + paramValue);
                        queryStringList.add(param + "=" + paramValue);
                    }
                } else if (Objects.equals(param, "keyword")) {
                     //TODO : 간단하게 like를 사용했으나 regexp를 사용해보는건 어떨까.
                     conditionsList.add("title like '%" + paramValue + "%'");
                     queryStringList.add("keyword=" + paramValue);

                } else if (Objects.equals(param, "page")){
                    Integer intVal = Integer.parseInt(paramValue);
                    Integer startPage = (intVal - 1) *10;
                    request.setAttribute("page", paramValue);
                    pageLimit = " LIMIT " + startPage.toString() + ", 10";
                    queryStringList.add("page=" + paramValue);
                } else {
                    if (!Objects.equals(paramValue, "init")) {
                        status = paramValue;
                    }
                }
            }
        }
    }
    //카테고리 id와 name을 가져온다.
    //TODO : 진행하면서 카테고리를 가져오는 클래스가 굳이 달라야하는지 확인한다. 필요없다면 합친다.
    try {
        request.setAttribute("categories", new CategoriesResults().categoriesSearch());
    } catch (RuntimeException e) {
        //TODO : 카테고리를 가져올 때 오류가 난다면 어떻게 처리해야하는지 정해야한다.
        System.out.println("initCategoryError" + e.getMessage());
    }

    DBActions dbActions = new DBActions();
    if (Objects.equals(status, "init")) {
        //1번 처리 과정.
        //total이 null이거나 0이면 목록 대신 모두 총 0건,'결과가 없습니다'를 띄운다.
        try {
            List<ContentsEntity> contents = dbActions.returnFullContents(null);
            if (!contents.isEmpty()) {
                request.setAttribute("status", status);
                request.setAttribute("total", contents.size());
                request.setAttribute("contents", contents);
                System.out.println("total count:  " + contents.size());
                System.out.println(contents.get(0).getContent());
            } else {
                //total으로 forEach 태그를 사용해야할지 여부를 확인한다.
                request.setAttribute("total", 0);
            }
        } catch (Exception e) {
            System.out.println("initContentsError" + e.getMessage());
            request.setAttribute("total", null);
        }
    } else {
        //2,3번 처리 과정.
        try {
            String conditions = String.join(" AND ", conditionsList);
            System.out.println("conditions sql: conditions list  - " + conditions);
            //게시글의 총 개수를 구하기 위해 limit 조건을 붙이기 전에 먼저 total 을 가져온다.
            Integer total = dbActions.lectureCountByConditions(conditions);
            request.setAttribute("total", total);
            System.out.println("total count:  " + total);

            if (pageLimit != null && pageLimit != "" && pageLimit.length() > 1)
                conditions = conditions + pageLimit;
            System.out.println("sql conditions:  " + conditions);
            List<ContentsEntity> contents = dbActions.returnFullContents(conditions);
            request.setAttribute("status", status);
            request.setAttribute("contents", contents);
        } catch (Exception e) {
            System.out.println("conditionalSearchError" + e.getMessage());
            request.setAttribute("total", null);
        }
    }
    String href = "board.jsp";
    if(queryStringList.size() > 0) href = href + "?" + String.join("&", queryStringList);
    System.out.println("href   : " + href +  "request url" + request.getRequestURL().toString());
    RequestDispatcher dispatcher = request.getRequestDispatcher (href);
    dispatcher.include(request, response);
%>