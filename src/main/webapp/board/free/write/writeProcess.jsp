<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="com.study.connection.DBActions" %>
<%@ page import="java.io.*, java.util.*, org.apache.commons.fileupload2.*,java.nio.charset.StandardCharsets, org.apache.commons.fileupload2.core.*, org.apache.commons.fileupload2.jakarta.servlet6.*" %>
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.study.connection.Encrypt" %>
<%@ page import="java.security.NoSuchAlgorithmException" %>
<%@ page import="com.study.connection.entity.ContentsEntity" %>
<%@ page import="com.study.connection.entity.FilesEntity" %>
<%
    request.setCharacterEncoding("UTF-8");
    //formaction으로 받은 input 값들을 통해 새로운 글을 생성한다.
    //스터디에서 제공된 예시에서 2024.01.23 14:03 같이 표현되어 있으므로 해당 형식으로 통일한다.
    String submitDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("y.M.d H:m"));
    boolean isMultipart = JakartaServletFileUpload.isMultipartContent( request);

    DiskFileItemFactory factory = new DiskFileItemFactory.Builder().get();
    JakartaServletFileUpload upload = new JakartaServletFileUpload(factory);
    String nickname = "";
    String title = "";
    String content = "";
    String password = "";
    String categoryId = "";
    List<FilesEntity> entities = new ArrayList<>(3);
    try {
        List<FileItem> items = upload.parseRequest(request);
        for (FileItem item : items) {
            if (item.isFormField()) {
                String fieldName = item.getFieldName();
                System.out.println("fieldName: " + fieldName);
                String fieldValue = item.getString(StandardCharsets.UTF_8);
                System.out.println("fieldValue: " + fieldValue);
                if(fieldName.equals("writeNickname")) {
                    nickname = fieldValue;
                    System.out.println("writeNickname String: " + nickname);
                    request.setAttribute("writeNickname", nickname);
                }else {
                    if(fieldName.equals("writeTitle")) {
                        title = fieldValue;
                        System.out.println("title: " + title);
                        request.setAttribute("title", title);
                    }else {
                        if(fieldName.equals("writeContent")) {
                            content = fieldValue;
                            System.out.println("content: " + content);
                            request.setAttribute("content", content);
                        }else {
                            if(fieldName.equals("provePassword") ||fieldName.equals("password")){
                                password = fieldValue;
                                System.out.println("provePassword: " + content);
                            }
                            else{
                                if(fieldName.equals("writeCategory")) {
                                categoryId = fieldValue;
                                System.out.println("writeCategory: " + categoryId);
                                request.setAttribute("categoryId", categoryId);}
                            }
                        }
                    }

                }
            } else {
                // 파일 필드 처리
                FilesEntity entity = new FilesEntity();
                String fieldName = item.getFieldName();
                System.out.println("fieldName: " + fieldName);
                String fileName = item.getName();
                System.out.println("fileName: " + fileName);
                if(fileName != null && fileName != "null" && fileName.length() > 0){
                    entity.setFileName(fileName);
                    entity.setFileOriginalName(fileName);
                    String fileExtension = fileName.substring(fileName.lastIndexOf(".")); // 파일 확장자
                    entity.setFileExtension(fileExtension);
                    String uploadPath =  "C:/Users/admin/file" + File.separator + fileName; // 업로드할 물리경로
                    System.out.println("uploadPath: " + uploadPath);
                    entity.setFilePath(uploadPath);
                    int sizeInBytes = (int) item.getSize();
                    System.out.println("sizeInBytes: " + sizeInBytes);
                    entity.setFileVolume(sizeInBytes);
                    InputStream inputStream = item.getInputStream();
                    try (FileOutputStream outputStream = new FileOutputStream(uploadPath)) {
                        byte [] bytes = new byte[sizeInBytes];
                        int exist = 0;
                        //exist는 버퍼에 있는 바이트의 길이를 의미. 더이상 읽어올 것이 없을 경우 -1을 반환.
                        while ((exist = inputStream.read(bytes)) != -1) {
                            outputStream.write(bytes , 0 , exist); //bytes = 내용 , 0 = 시작지점 , exist = 남아있는 총 길이
                        }
                    } catch (IOException io) {
                        System.out.println("writeProcessFileUploading :   " + io.getMessage());
                    }
                    inputStream.close();
                    entities.add(entity);


                }
            }
        }
    } catch (Exception e) {
        System.out.println("File upload failed: " + e.getMessage());
    }
    // contents table 에 내용을 저장한다.
    try {
        String encrypt = new Encrypt().Encryption(password);
        String sql = "INSERT INTO contents(title,content,password,nickname,contentCategoryId,viewCount,submitDate) VALUES(" +
                "'" + title + "','" + content + "','" + encrypt + "','" + nickname + "'," + categoryId + "," + 0 + ",STR_TO_DATE('" + submitDate + "','%Y.%m.%d %H:%i'));";
        System.out.println("sql: " + sql);
        DBActions dbActions = new DBActions();
        dbActions.updateDB(sql);
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
    }

    //files db에 넣기 위해서는 contentId가 필요하기 때문에 먼저 contents db에 내용을 넣고 난 뒤에 contentId를 가져온다.
    try {
        String forGetContentId = " nickname='" + nickname + "' AND title='" + title + "' AND contentCategoryId=" + categoryId + " AND STR_TO_DATE('" + submitDate + "','%Y.%m.%d %H:%i');"
        System.out.println("forGetContentIdSql :    " + forGetContentId);
        List<ContentsEntity> entity = new DBActions().returnFullContents();
        request.setAttribute("contentId" , entity.get(0).getContentId());
    } catch (Exception e) {
        System.out.println("writeProcessForGetContentId :    " + e.getMessage());
    }
    if(entities.size() > 0){
        for (FilesEntity files : entities){
            if(files != null){
                try {
                    new DBActions().updateDB("INSERT INTO files(contentIdHaveFile,fileName,filePath,fileVolume,fileOriginalName,fileExtension) VALUES(" +
                    request.getAttribute("contentId") + ",'" + files.getFileName() + "','" + files.getFilePath() + "'," + files.getFileVolume() + ",'" + files.getFileName() + "','" + files.getFileExtension() + "');");
                } catch (Exception e) {
                    System.out.println("writeProcessInsertToFilesDB  :   " + e.getMessage());
                }
            }
        }
    }
    //view.jsp를 위한 변환 작업. password는 제외.
    request.setAttribute("title" , title);
    request.setAttribute("content" , content);
    request.setAttribute("nickname" , nickname);
    request.setAttribute("contentCategoryId" , categoryId);
    request.setAttribute("submitDate" , submitDate);
    System.out.println("request.getRequestURL().toString()  :   " + request.getRequestURL().toString() + request.getAttribute("contentId"));
    //db에 집어넣는 작업이 완료되면 board.jsp로 이동하지 않고 view.jsp로 이동해 해당 내용으로 만들어낸 새로운 글을 보여준다.
    response.sendRedirect(request.getRequestURL().toString()
            .replace("http://localhost:8080/eb-study-template-1week" , "").replace("write/writeProcess.jsp" , "view/view.jsp") + "?contentId=" + request.getAttribute("contentId"));
%>
