<%@ page import="com.study.connection.DBActions" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.io.File" %>
<%@ page import="java.io.FileOutputStream" %>
<%@ page import="java.io.IOException" %>
<%@ page import="com.study.connection.entity.FilesEntity" %>
<%@ page import="com.study.connection.dao.FilesDAO" %>
<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8" %>

<%
    request.setCharacterEncoding("UTF-8");

    String content = request.getParameter("modifyContent");
    String user = request.getParameter("modifyWriter");
    String title = request.getParameter("modifyTitle");
    String content_id = request.getParameter("contentId");

    List<String> files = new ArrayList<>(Arrays.asList(request.getParameter("writeFile1") ,
            request.getParameter("writeFile2") , request.getParameter("writeFile3")));

    for (String file : files) {
        if (file != null) {
            Part filePart = (Part) request.getPart(file); //form 태그 중 name = file 인 값을 inputStream 받기 위한 객체
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

            // 업로드된 파일 내용을 db에 저장한다.
            try {
                FilesEntity filesEntity = new FilesEntity();
                filesEntity.setFileOriginalName(fileName);
                filesEntity.setFileVolume(fileSize);
                filesEntity.setFilePath(uploadPath);
                filesEntity.setFileName(fileName);
                filesEntity.setContentIdHaveFile((int) request.getAttribute("content_id"));
                filesEntity.setFileExtension(fileExtension);

                new FilesDAO(filesEntity).update();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }

    try {
        new DBActions().updateDB("UPDATE contents SET nickname='" +user + "',title='" + title + "',content='" + content + "',"
                + " WHERE content_id=" + content_id + ";");
    } catch (Exception e) {
        System.out.println("updateModifyError :   " + e.getMessage());
    }

    request.getRequestDispatcher(request.getRequestURL().toString().replace("http://localhost:8080" , "")
            .replace("modify/updateModify.jsp" , "board/board.jsp")).forward(request , response);
%>