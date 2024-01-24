<%@ page import="com.study.connection.CategoriesResults" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="com.study.connection.DBActions" %>
<%@ page import="com.study.connection.Encrypt" %>
<%@ page import="java.security.NoSuchAlgorithmException" %>
<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8"%>
<%
    //밑에 것과 같이 합치던가 아니면 따로 jsp를 생성하던가 해야한다. 아니면 둘다 실행된다.
    request.setCharacterEncoding("UTF-8");
    //write.jsp의 처음 화면에 필요한 카테고리 내용들을 제공한다.
    try{
        Map<Integer,String> writeCategories = new CategoriesResults().categoriesSearch();
        request.setAttribute("categories" , writeCategories);
    }catch (RuntimeException e){
        System.out.println("writeProcessCategoryError :" + e.getMessage());
    }
%>

<%
    request.setCharacterEncoding("UTF-8");
    //formaction으로 받은 input 값들을 통해 새로운 글을 생성한다.
    //db에 집어넣는 작업이 완료되면 board.jsp로 이동하지 않고 view.jsp로 이동해 해당 내용으로 만들어낸 새로운 글을 보여준다.
    String title = request.getParameter("writeTitle");
    String content = request.getParameter("writeContent");
    String password = request.getParameter("password");
    String nickname = request.getParameter("writeNickname");
    //처음 화면을 위해 attribute로 저장해둔 categories가 남아있는지 여부 확인 필요하다.
    String categoryId = request.getParameter("writeCategory");
    //스터디에서 제공된 예시에서 2024.01.23 14:03 같이 표현되어 있으므로 해당 형식으로 통일한다.
    String submitDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("%y.%M.%d %H:%m"));
    //TODO : 파일 첨부 기능이 없기 때문에 파일변수는 만들지 않았다. 구현 후에는 추가 필요.

    try {
        String encrypt = new Encrypt().Encryption(password);
        String sql = "INSERT INTO contents(title,content,password,nickname,content_category_id,view_count,submit_date) VALUES(" +
                "'" + title + "','" + content + "','" + encrypt + "','" + nickname + "'," + categoryId + "," + 0 + ",STR_TO_DATE('" + submitDate + "','%Y.%m.%d %H:%i');";
        DBActions dbActions = new DBActions();
        //TODO : error 발생 시에 필요한 조치를 취하지 않음. 하지만 try catch를 하더라도 딱히 별 의미가 없는 상태.
        dbActions.updateDB(sql);
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
    }

    //view.jsp를 위한 변환 작업. password는 제외. content_id는 view.jsp에서 가져온다.
    request.setAttribute("title" , title);
    request.setAttribute("content" , content);
    request.setAttribute("nickname" , nickname);
    request.setAttribute("content_category_id" , categoryId);
    request.setAttribute("submit_date" , submitDate);

    RequestDispatcher requestDispatcher = request.getRequestDispatcher(request.getRequestURL().toString().replace("http://localhost:8080" , "").replace("write/writeProcess.jsp" , "view/viewProcess.jsp"));
    requestDispatcher.forward(request,response);
%>
