<%@ page import="com.study.connection.DBActions" %>
<%@ page import="java.io.*, java.util.*, org.apache.commons.fileupload2.*,java.nio.charset.StandardCharsets, org.apache.commons.fileupload2.core.*, org.apache.commons.fileupload2.jakarta.servlet6.*" %>
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<%
    request.setCharacterEncoding("UTF-8");
    String content_id = request.getParameter("contentId");
    boolean isMultipart = JakartaServletFileUpload.isMultipartContent( request);
    if (isMultipart) {
        List<String> stringList = new ArrayList<>(4);
        DiskFileItemFactory factory = new DiskFileItemFactory.Builder().get();
        JakartaServletFileUpload upload = new JakartaServletFileUpload(factory);
        String nickname = "";
        String title = "";
        String content = "";
        String contentId = "";
        try {
            List<FileItem> items = upload.parseRequest(request);
            for (FileItem item : items) {
                if (item.isFormField()) {
                    String fieldName = item.getFieldName();
                    System.out.println("fieldName: " + fieldName);
                    String fieldValue = item.getString(StandardCharsets.UTF_8);
                    System.out.println("fieldValue: " + fieldValue);
                    if(fieldName.equals("modifyWriter")) {
                        nickname = fieldValue;
                        System.out.println("nickname String: " + nickname);
                        stringList.add("nickname='" + fieldValue + "'");
                        System.out.println("nickname: " + stringList);
                    }else {
                        if(fieldName.equals("modifyTitle")) {
                            title = fieldValue;
                            System.out.println("title: " + title);
                            stringList.add("title='%" + fieldValue + "%'");
                            System.out.println("modifyTitle: " + stringList);

                        }else {
                            if(fieldName.equals("modifyContent")) {
                                content = fieldValue;
                                System.out.println("content: " + content);
                                stringList.add("content='%" + fieldValue + "%'");
                                System.out.println("modifyContent: " + stringList);
                            }else {
                                contentId = fieldValue;
                                System.out.println("contentId: " + contentId);
                                stringList.add("WHERE contentId=" + fieldValue + ";");
                                System.out.println("contentId" + stringList);
                            }
                        }

                    }
                } else {
                    // 파일 필드 처리
                    String fieldName = item.getFieldName();
                    System.out.println("fieldName: " + fieldName);
                    String fileName = item.getName();
                    System.out.println("fileName: " + fileName);
                    String contentType = item.getContentType();
                    System.out.println("contentType: " + contentType);
                    boolean isInMemory = item.isInMemory();
                    long sizeInBytes = item.getSize();
                    System.out.println("File Name: " + fileName + ", Size: " + sizeInBytes + " bytes<br>");
                }
            }
            try {
                String sql = "UPDATE contents SET nickname='" + nickname + "', title='" + title + "', content='"
                    + content + "' WHERE contentId=" + contentId + ";";
                System.out.println("sql : " + sql);
                new DBActions().updateDB(sql);
            } catch (Exception e) {
                System.out.println("updateModifyError :   " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("File upload failed: " + e.getMessage());
        }
    }

    RequestDispatcher requestDispatcher = request.getRequestDispatcher(request.getRequestURL().toString().replace("http://localhost:8080/eb-study-template-1week" , "").replace("modify/updateModify.jsp" , "list/board.jsp"));
    requestDispatcher.forward(request , response);
%>