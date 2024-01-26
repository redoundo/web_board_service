package com.study.connection.controller;

import com.study.connection.dto.FileDto;
import com.study.connection.service.FilesService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  write.jsp , modify.jsp 에서 form 엑션으로 값이 넘어오는 것들은 fileEntity 뿐만 아니라 contentEntity 와 함께 들어오므로
 *  insert ,  update 기능은 전부 ContentController 에서 진행.
 */
@MultipartConfig
@WebServlet(urlPatterns = "/FileController" , name = "FileController")
public class FileController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        //super.doGet(req, res);
        //select 기능
        try{

            String id = req.getParameter("content_id");
            List<FileDto> list = new FilesService().select(id);
            req.setAttribute("files" , list);

            String path = new PathMapper().mapper("view" , req.getRequestURL().toString());
            new ToView().include(req , res , path);

        } catch (Exception e) {
            res.setContentType("text/html; charset=UTF-8");
            PrintWriter out = res.getWriter();
            out.println("<script>alert('오류가 발생했습니다. 새로고침 해주시기 바랍니다.')</script>");
            out.flush();
            out.close();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        //super.doDelete(req, res);
        //delete 기능
        res.setContentType("text/html; charset=UTF-8");
        PrintWriter out = res.getWriter();
        try {

            String id = req.getParameter("content_id");
            new FilesService().delete(id);

            out.println("<script>alert('삭제가 완료되었습니다.')</script>");
            out.flush();

            String path = new PathMapper().mapper("board" , req.getRequestURL().toString());
            new ToView().redirect(res , path);

        } catch (Exception e){

            out.println("<script>alert('내용을 삭제하는데 오류가 발생했습니다. " +
                    "잠시 후 다시 시도해주시기 바랍니다.')</script>");
            out.flush();

        } finally {
            out.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        //super.doPost(req, res);
        //request 에 존재하는 파일 이름들 리스트화
        try {
            List<Part> parts = new ArrayList<>(Arrays.asList(req.getPart("file1"),
                req.getPart("file2"), req.getPart("file3")));
            String id = req.getParameter("content_id");
            new FilesService().insert(new LoadFiles().upload(parts , id)); // db 에 fileEntity insert 진행.
        } catch (Exception e) {
            res.setContentType("text/html; charset=UTF-8");
            PrintWriter out = res.getWriter();
            out.println("<script>alert('내용을 저장하는데 오류가 발생했습니다. " +
                    "잠시 뒤에 다시 시도해주세요.')</script>");//다시 form 제출 하게끔 안내.
            out.flush();
            out.close();//redirect 하지 않음.
        }
    }
}
