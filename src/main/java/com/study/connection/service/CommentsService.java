package com.study.connection.service;

import com.study.connection.dao.CommentDao;
import com.study.connection.entity.CommentEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommentsService {

    public List<CommentEntity> select(@NotNull String contentId) throws Exception{
        if(!contentId.isEmpty()) {
            try {
                Integer id = Integer.parseInt(contentId);
                return new CommentDao().select(id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new Exception();
        }
    }

    public void insert (@NotNull CommentEntity entity) throws Exception {
        if(entity.getCommentedContentId() != null && entity.getComment() != null && entity.getCommentUser() != null
                && entity.getCommentedDate() != null) {
           try{
               new CommentDao().insert(entity);
           } catch (Exception e) {
               throw new RuntimeException(e);
           }
        } else {
           throw new Exception();
        }
    }

    public void delete (@NotNull String commentId) throws Exception{
        if(!commentId.isEmpty()) {
            try{
                Integer id = Integer.parseInt(commentId);
                new CommentDao().delete(id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new Exception();
        }
    }
}
