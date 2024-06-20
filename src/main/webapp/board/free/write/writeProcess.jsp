<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="com.study.connection.DBActions" %>
<%@ page import="com.study.connection.Encrypt" %>
<%@ page import="java.security.NoSuchAlgorithmException" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="com.study.connection.entity.ContentsEntity" %>
<%@ page import="java.io.FileOutputStream" %>
<%@ page import="java.io.File" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.io.IOException" %>
<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8"%>
<%
    request.setCharacterEncoding("UTF-8");
    //formaction으로 받은 input 값들을 통해 새로운 글을 생성한다.
    String title = request.getParameter("writeTitle");
    String content = request.getParameter("writeContent");
    String password = request.getParameter("password");
    String nickname = request.getParameter("writeNickname");
    String categoryId = request.getParameter("writeCategory");
    //스터디에서 제공된 예시에서 2024.01.23 14:03 같이 표현되어 있으므로 해당 형식으로 통일한다.
    String submitDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("%y.%M.%d %H:%m"));

    try {
        String encrypt = new Encrypt().Encryption(password);
        String sql = "INSERT INTO contents(title,content,password,nickname,content_category_id,view_count,submit_date) VALUES(" +
                "'" + title + "','" + content + "','" + encrypt + "','" + nickname + "'," + categoryId + "," + 0 + ",STR_TO_DATE('" + submitDate + "','%Y.%m.%d %H:%i');";
        DBActions dbActions = new DBActions();
        dbActions.updateDB(sql);
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
    }

    //files db에 넣기 위해서는 content_id가 필요하기 때문에 먼저 contents db에 내용을 넣고 난 뒤에 content_id를 가져온다.
    try {
        List<ContentsEntity> entity = new DBActions().returnFullContents("nickname='" + nickname +
                "' AND title='" + title + "' AND content_category_id=" + categoryId + ";");
        request.setAttribute("content_id" , entity.get(0).getContentId());
    } catch (Exception e) {
        System.out.println("writeProcessForGetContent_id :    " + e.getMessage());
    }

    List<String> files = new ArrayList<>(Arrays.asList(request.getParameter("writeFile1") ,
            request.getParameter("writeFile2") , request.getParameter("writeFile3")));

    for (String file : files) {
        if (file != null) {
            Part filePart = request.getPart(file); //form 태그 중 name = file 인 값을 inputStream 받기 위한 객체
            String fileName = filePart.getSubmittedFileName(); //파일 이름
            String fileExtension = fileName.substring(fileName.lastIndexOf(".")); // 파일 확장자
            String uploadPath =  "C:/Users/admin/file" + File.separator + fileName; // 업로드할 물리경로
            int fileSize = (int) filePart.getSize();

            InputStream inputStream = filePart.getInputStream();
            try (FileOutputStream outputStream = new FileOutputStream(uploadPath)) {
                byte [] bytes = new byte[fileSize];
                int exist = 0;
                //exist는 버퍼에 있는 바이트의 길이를 의미. 더이상 읽어올 것이 없을 경우 -1을 반환.
                while ((exist = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes , 0 , exist); //bytes = 내용 , 0 = 시작지점 , exist = 남아있는 총 길이
                }
            } catch (IOException io) {
                System.out.println("writeProcessFileUploading :   " + io.getMessage());
            }
            inputStream.close();

            try {
                new DBActions().updateDB("INSERT INTO files(content_id_have_file,file_name,file_path,file_volume,file_original_name,file_extension) VALUES(" +
                request.getAttribute("content_id") + ",'" + fileName + "','" + uploadPath + "'," + fileSize + ",'" + fileName + "','" + fileExtension + "');");
            } catch (Exception e) {
                System.out.println("writeProcessInsertToFilesDB  :   " + e.getMessage());
            }
        }
    }

    //view.jsp를 위한 변환 작업. password는 제외.
    request.setAttribute("title" , title);
    request.setAttribute("content" , content);
    request.setAttribute("nickname" , nickname);
    request.setAttribute("content_category_id" , categoryId);
    request.setAttribute("submit_date" , submitDate);

    //db에 집어넣는 작업이 완료되면 board.jsp로 이동하지 않고 view.jsp로 이동해 해당 내용으로 만들어낸 새로운 글을 보여준다.
    RequestDispatcher requestDispatcher = request.getRequestDispatcher(request.getRequestURL().toString()
            .replace("http://localhost:8080" , "").replace("write/writeProcess.jsp" , "view/viewProcess.jsp"));
    requestDispatcher.forward(request,response);
%>
