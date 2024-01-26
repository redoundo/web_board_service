<%@ page import="java.io.*" %>
<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8"%>

<%
    String path = request.getParameter("path");
    String name = request.getParameter("name");
    response.setContentType("application/x-msdownload");
    response.setHeader("Content-Disposition" , "attachment;filename='" + name + "'");
    File file = new File(path);

    BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
    BufferedOutputStream output = new BufferedOutputStream(response.getOutputStream());
    try{
        byte [] bytes = new byte[(int) file.length()];
        int data = 0;
        while ((data = input.read(bytes)) != -1) {
            output.write(bytes , 0 ,data);
        }
        input.close();
        output.close();
        out.clear();

        out = pageContext.pushBody();
        out.close();
    } catch (Exception e) {
        e.getStackTrace();
    }

    request.getRequestDispatcher(request.getRequestURL().toString().replace("http://localhost:8080" , "")
            .replace("downloadFile.jsp" , "view.jsp")).include(request , response);
%>