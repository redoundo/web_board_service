<%@ page import="com.study.connection.DBActions" %>
<%@ page import="java.io.*, java.util.*, org.apache.commons.fileupload2.*, org.apache.commons.fileupload2.core.*, org.apache.commons.fileupload2.jakarta.servlet6.*" %>
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<%
    request.setCharacterEncoding("UTF-8");
    boolean isMultipart = JakartaServletFileUpload.isMultipartContent( request);
    if (isMultipart) {
        DiskFileItemFactory factory = new DiskFileItemFactory.Builder().get();
        JakartaServletFileUpload upload = new JakartaServletFileUpload(factory);
        try {
            List<FileItem> items = upload.parseRequest(request);
            for (FileItem item : items) {
                if (item.isFormField()) {
                    // 일반 폼 필드 처리
                    String fieldName = item.getFieldName();
                    String fieldValue = item.getString("UTF-8");
                    out.println("Field Name: " + fieldName + ", Value: " + fieldValue + "<br>");
                } else {
                    // 파일 필드 처리
                    String fieldName = item.getFieldName();
                    String fileName = item.getName();
                    String contentType = item.getContentType();
                    boolean isInMemory = item.isInMemory();
                    long sizeInBytes = item.getSize();

                    // 파일 저장 (예시)
                    File uploadedFile = new File("C:/uploads/" + fileName);
                    item.write(uploadedFile);

                    out.println("File Name: " + fileName + ", Size: " + sizeInBytes + " bytes<br>");
                }
            }
        } catch (Exception e) {
            out.println("File upload failed: " + e.getMessage());
        }
    }

    String content = request.getParameter("modifyContent");
    String user = request.getParameter("modifyWriter");
    String title = request.getParameter("modifyTitle");
    String content_id = request.getParameter("contentId");

    try {
        System.out.println("contentId == " + content_id + user + title + content);
        new DBActions().updateDB("UPDATE contents SET nickname='" +user + "',title='" + title + "',content='" + content + "',"
                + " WHERE contentId=" + content_id + ";");
    } catch (Exception e) {
        System.out.println("updateModifyError :   " + e.getMessage());
    }

    RequestDispatcher requestDispatcher = request.getRequestDispatcher(request.getRequestURL().toString().replace("http://localhost:8080/eb-study-template-1week" , "").replace("modify/updateModify.jsp" , "list/board.jsp"));
    requestDispatcher.forward(request , response);
%>