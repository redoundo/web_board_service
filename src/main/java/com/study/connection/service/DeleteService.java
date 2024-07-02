package com.study.connection.service;

import com.study.connection.mapper.*;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@MultipartConfig
@RequiredArgsConstructor
@Lazy
public class DeleteService {
    private final CommentMapper commentMapper;
    private final FileMapper fileMapper;

    private final NotifyMapper notifyMapper;
    private final BoardMapper boardMapper;
    private final QuestionMapper questionMapper;


    public void deleteNotification(Integer contentId, Integer admin){
        this.notifyMapper.deleteNotification(contentId, admin);
    }
    public void deleteQuestion(Integer contentId, Integer userId){
        this.questionMapper.deleteQuestion(contentId, userId);
    }

    /**
     * 댓글 삭제
     * @param contentId 게시글 아이디
     * @param userId 댓글을 작성한 사용자의 아이디
     * @param commentId 삭제할 댓글
     */
    public void deleteAComment(Integer contentId, Integer userId, Integer commentId){
        this.commentMapper.deleteAComment(contentId, userId, commentId);
    }

    /**
     * 게시글 삭제
     * @param contentId 게시글 아이디
     * @param userId 사용자 아이디
     * @param table 삭제할 게시글이 들어 있는 테이블 이름
     */
    public void deleteAContent(Integer contentId, Integer userId, String table){
        switch (table){
            case "question":
                this.questionMapper.deleteQuestion(contentId, userId);
                break;
            case "board", "gallery":
                this.boardMapper.deleteContentById(contentId, userId);
                break;
            case "notify":
                this.notifyMapper.deleteNotification(contentId, userId);
                break;
        }
    }
}
