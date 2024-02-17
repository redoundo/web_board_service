package com.study.connection.controller;

import com.study.connection.dto.*;
import com.study.connection.entity.CommentEntity;
import com.study.connection.service.CommentService;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * view.html 에서 comment 등록 , 파일 다운로드 기능 제공한다.
 */
@Controller
@MultipartConfig
public class CommentFileController {
    /**
     *
     * @param model
     * @param comment
     * @return
     */
    @RequestMapping(value = "/view/comment" , method = RequestMethod.POST)
    public String addComment(Model model , @ModelAttribute CommentDto comment){
        try{
            CommentEntity entity = CommentEntity.builder()
                    .comment(comment.getComment())
                    .commentedContentId(comment.getCommentedContentId())
                    .commentUser(comment.getCommentUser())
                    .commentedDate(Date.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("y-M-d"))))
                    .build();
            new CommentService().insertComment(entity);
        } catch (Exception e){
            //TODO : /view/comment 로 온 상태에서 다른 내용이 유지될지 확인해보자.
            model.addAttribute("newComment" , comment);//저장되어야했던 내용을 다시 넣어준다
            model.addAttribute("commentError" ,
                    "댓글 저장을 하는 도중 오류가 발생했습니다. " +
                    "잠시 뒤에 다시 시도해주시기 바랍니다.");
        }
        return "view";
    }

    /**
     * 파일 다운로드 기능.
     * @param path 파일 경로
     * @param name 파일 이름
     * @param response 다운로드할 사용자의 response
     */
    @RequestMapping(value = {"/view/download" , "/modify/download"} , method = RequestMethod.POST)
    public void downloadFile(@RequestParam("filePath") String path , @RequestParam("fileName") String name ,
                               HttpServletResponse response){
        try{
            response.setContentType("application/download; utf-8");
            response.setHeader( "Content-Disposition", "attachment; filename=\"" +  URLEncoder.encode(name,"UTF-8") + "\";");
            response.setHeader("Content-Transfer-Encoding", "binary");

            URL url = new URL(path);
            OutputStream output = response.getOutputStream();
            try (InputStream input = url.openStream()) {
                byte [] bytes = new byte[5*1024*1024];
                int data = 0;
                while ((data = input.read(bytes)) != -1) {
                    output.write(bytes , 0 ,data);
                }
                input.close();
                output.flush();
            }
        }catch (Exception e){
            e.getStackTrace();
        }

    }
}
