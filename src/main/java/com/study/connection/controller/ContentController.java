package com.study.connection.controller;

import com.study.connection.dto.ContentDto;
import com.study.connection.service.ContentsService;
import com.study.connection.service.FilesService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.*;

/**
 * board/free/*\/*\/ContentController || /index/ContentController 에서 request 요청이 들어올 것이라 예상
 * doPost , doPut 에서는 request 에서 file 관련 내용을 가지고 있기 때문에 FileController 의 몫까지 같이 진행.
 */
@WebServlet(urlPatterns = "/board/free/*/*/ContentController" , name = "ContentController")
public class ContentController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        super.doPost(req, res);

        //insert 용도 => write.jsp 에서 post 요청이 오면 view.jsp 로 sendRedirect 진행.
        req.setCharacterEncoding("UTF-8");
        ContentDto dto = ContentDto.builder()
                .content(req.getParameter("content"))
                .contentCategoryId(Integer.parseInt(req.getParameter("content_category_id")))
                .title(req.getParameter("title"))
                .submitDate(Date.valueOf(req.getParameter("submit_date")))
                .password(req.getParameter("password"))
                .viewCount(0)
                .nickname(req.getParameter("nickname"))
                .build();

        try{

            //request 에 존재하는 파일 이름들 리스트화
            List<Part> parts = new ArrayList<>(Arrays.asList(req.getPart("file1"),
                    req.getPart("file2"), req.getPart("file3")));
            String id = req.getParameter("content_id");

            new FilesService().insert(new LoadFiles().upload(parts , id)); // db 에 fileEntity insert 진행.
            new ContentsService().insert(dto);//db 에 contentEntity insert 진행

            //해당 content_id 값을 찾기 위해 쿼리스트링 제작
            Map<String , String> query = new HashMap<>();
            query.put("title" , req.getParameter("title"));
            query.put("nickname" , req.getParameter("nickname"));
            query.put("content_category_id" , req.getParameter("content_category_id"));

            String path = new PathMapper().mapping("view" , query); //write.jsp -> view.jsp
            new ToView().redirect(res , path);//query 로 찾은 내용이 있을 view.jsp 로 이동.

        } catch (Exception e) {

            res.setContentType("text/html; charset=UTF-8");
            PrintWriter out = res.getWriter();
            out.println("<script>alert('내용을 저장하는데 오류가 발생했습니다. " +
                    "잠시 뒤에 다시 시도해주세요.')</script>");//다시 form 제출 하게끔 안내.
            out.flush();
            out.close();//redirect 하지 않음.

        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        super.doDelete(req, res);
        //delete 용도
        res.setContentType("text/html; charset=UTF-8");
        PrintWriter out = res.getWriter();

        try {

            new ContentsService().delete(req.getParameter("content_id"));

            out.println("<script>alert('게시물이 삭제되었습니다.')</script>");
            out.flush();

            String path = new PathMapper().mapper("board" ,
                    req.getRequestURL().toString());// view.jsp -> board.jsp
            new ToView().redirect(res , path);

        } catch (Exception e) {

            out.println("<script>alert('내용을 삭제하는데 오류가 발생했습니다. " +
                    "잠시 뒤에 다시 시도해주세요.')</script>");
            out.flush();

        } finally {
            out.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        super.doGet(req, res);
        //select 용도

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        super.doPut(req, res);
        //update 용도
        ContentDto dto = ContentDto.builder()
                .contentId(Integer.parseInt(req.getParameter("content_id")))
                .nickname(req.getParameter("nickname"))
                .title(req.getParameter("title"))
                .content(req.getParameter("content"))
                .build();

        try {
            List<Part> parts = new ArrayList<>(Arrays.asList(req.getPart("file1"),
                    req.getPart("file2"), req.getPart("file3")));
            String id = req.getParameter("content_id");
            new FilesService().insert(new LoadFiles().upload(parts , id)); // db 에 fileEntity insert 진행.
            new ContentsService().update(dto);

            Map<String , String> query = new HashMap<>();
            query.put("content_id" , req.getParameter("content_id"));

            String path = new PathMapper().mapping("view" , query);//modify.jsp -> 내용이 변경된 view.jsp
            new ToView().redirect(res , path);//view.jsp 로 redirect.

        } catch (Exception e) {

            res.setContentType("text/html; charset=UTF-8");
            PrintWriter out = res.getWriter();
            out.println("<script>alert('내용을 변경하는데 오류가 발생했습니다. " +
                    "잠시 후에 다시 시도해주세요.')</script>");
            out.flush();
            out.close();//이동하지 않고 다시 요청 하게끔 안내.

        }
    }
    
}
