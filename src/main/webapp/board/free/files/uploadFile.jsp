<%@ page import="java.io.File" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.io.FileOutputStream" %>
<%@ page import="java.io.IOException" %>
<%@ page import="com.study.connection.DBConnection" %>
<%@ page import="com.study.connection.DBActions" %>
<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8"%>
<%
    request.setCharacterEncoding("UTF-8");
    int content_id =  1;
    String savePath = "C:/Users/admin/file"; // 저장할 전체 파일 경로
    Part filePart = request.getPart("file"); //form 태그 중 name = file 인 값을 inputStream 받기 위한 객체
    String fileName = filePart.getSubmittedFileName();
    String uploadPath = savePath + File.separator +fileName; // 업로드 경로
    String fileExtension = fileName.substring(fileName.lastIndexOf("."));//파일 확장자
    int fileSize = (int) filePart.getSize();

    InputStream fileStream = filePart.getInputStream();
    try (FileOutputStream fileOutputStream = new FileOutputStream(uploadPath)) {
        byte[] bytes = new byte[fileSize];
        int size = 0;
        while ((size = fileStream.read(bytes)) != -1) {//size는 버퍼에 있는 바이트의 길이를 의미. 더이상 읽어올 것이 없을 경우 -1을 반환.
            fileOutputStream.write( bytes , 0 , size ); //bytes = 내용 , 0 = 시작지점 , size = 총 길이
        }
    } catch (IOException io) {
        System.out.println("FileUploadIoError :   " + io.getMessage());
    }
    fileStream.close();

    try {
        String uploadSql = "INSERT INTO files(content_id_have_file,file_name,file_volume,file_path,file_original_name,file_extension) VALUES(" +
                1 + ",'" + fileName + "'," + fileSize + ",'" + uploadPath + "','" + fileName + "','" + fileExtension + "');";
        new DBActions().updateDB(uploadSql);
    } catch (Exception e) {
        System.out.println("ErrorWhileUploading :  " + e.getMessage());
    }

%>