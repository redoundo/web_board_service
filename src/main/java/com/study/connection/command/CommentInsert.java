package com.study.connection.command;

import com.study.connection.entity.CommentEntity;
import com.study.connection.service.CommentsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.Date;

/**
 * comment 를 db에 저장한다.
 */
public class CommentInsert implements Command{
    @Override
    public void run(HttpServletRequest req, HttpServletResponse res) throws Exception {

        try{
            CommentEntity entity = CommentEntity.builder()
                    .commentUser(req.getParameter("comment_user"))
                    .comment(req.getParameter("comment"))
                    .commentedDate(Date.valueOf(req.getParameter("commented_date")))
                    .commentedContentId(Integer.parseInt(req.getParameter("commented_content_id")))
                    .build();
            new CommentsService().insert(entity);

        } catch (Exception e){
            System.out.println("CommentInsert.java   :     "  +  e.getMessage());
            throw  new Exception(e);
        }
    }
}
