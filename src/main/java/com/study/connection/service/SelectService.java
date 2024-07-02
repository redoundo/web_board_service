package com.study.connection.service;


import com.study.connection.dto.*;
import com.study.connection.entity.CommentEntity;
import com.study.connection.entity.ImageEntity;
import com.study.connection.mapper.*;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * contents , files , comments , categories
 */
@Service
@RequiredArgsConstructor
public class SelectService {
    private final FileMapper fileMapper;
    private final CategoryMapper categoryMapper;
    private final CommentMapper commentMapper;
    private final NotifyMapper notifyMapper;
    private final QuestionMapper questionMapper;
    private final BoardMapper boardMapper;


    /**
     * 게시글 아이디로 해당 게시글의 내용을 가져온다.
     * @param id 게시글 아이디
     * @param table 게시글을 찾을 테이블 이름
     * @return 게시글 내용.
     */
    public Map<String, Object> getContentById(@NotNull Integer id, @NotNull String table){
        Map<String, Object> result = new HashMap<>();
        switch (table){
            case "notify":
                Map<String, Object>  notifyResult = this.notifyMapper.getNotifyContentById(id);
                if(!notifyResult.isEmpty()) result.put("contents", notifyResult);
                break;
            case "board":
                Map<String, Object> contentResult = this.boardMapper.getContentByBoardId(id);
                if(!contentResult.isEmpty())  result.put("contents", contentResult);

                List<ImageEntity> images = this.fileMapper.existFileByPostId(id);
                if(!images.isEmpty()) result.put("files", images);
                //gallery 와 달리 댓글이 존재하므로 댓글 내용들을 가져온다.
                List<CommentDto> comments = this.commentMapper.selectComments(id);
                if(!comments.isEmpty()) result.put("comments", comments);

                if(result.isEmpty()) result = null; // 없으면 아예 null 로 바꾼다.
                break;
            case "gallery":
                Map<String, Object> galleryResult = this.boardMapper.getContentByBoardId(id);
                if(!galleryResult.isEmpty()) result.put("contents", galleryResult);

                List<ImageEntity> galleryImages = this.fileMapper.existFileByPostId(id);
                if(!galleryImages.isEmpty()) result.put("files", galleryImages);

                if(result.isEmpty()) result = null;
                break;
            case "question":
                Map<String, Object> questionResult = this.questionMapper.getQuestionByQuestionId(id);
                if(!questionResult.isEmpty()) result.put("contents", questionResult);

                //문의 게시글에 달린 답글을 가져온다.
                List<CommentDto> commentsDto = this.commentMapper.selectComments(id);
                if(!commentsDto.isEmpty()) result.put("comments", commentsDto);

                if(result.isEmpty()) result = null;
                break;
        }
        return result;
    }
    /**
     * 테이블 이름에 따라 다른 카테고리 반환.
     * @param table 테이블 이름.
     * @return 카테고리들.
     */
    public List<CategoryDto> getAllCategories(@NotNull String table){
        return switch (table) {
            case "notify" -> this.categoryMapper.getAllNotifyCategory();
            case "board", "gallery" -> this.categoryMapper.allCategories();
            default -> null;
        };
    }

    /**
     * 최신순으로 특정 페이지 양만큼만을 가져와 반환한다.
     * @param limits 가져올 페이지 양
     * @return 페이지 양 만큼 가져와진 내용들.
     */
    public Map<String, ResultDto> limitedResultNoCondition(SampleLimits limits){
        Integer questionLimit = limits.getQuestion();
        Integer boardLimit = limits.getBoard();
        Integer galleryLimit = limits.getGallery();
        Integer notifyLimit = limits.getNotify();

        List<Map<String, Object>> notifications = this.notifyMapper.limitedNotification(notifyLimit);
        List<Map<String, Object>> questions = this.questionMapper.limitedQuestions(questionLimit);
        List<Map<String, Object>> boards = this.boardMapper.limitedBoard(boardLimit, 0);
        List<Map<String, Object>> galleries = this.boardMapper.limitedGallery(galleryLimit, 1);

        Integer questionTotal = this.questionMapper.totalCount();
        Integer boardTotal = this.boardMapper.totalCount(0);
        Integer galleryTotal = this.boardMapper.totalCount(1);
        Integer notifyTotal = this.notifyMapper.totalCount();

        Map<String, ResultDto> map = new HashMap<>();
        map.put("notify", ResultDto.builder()
                .total(notifyTotal)
                .results(notifications)
                .build());
        map.put("question", ResultDto.builder()
                .results(questions)
                .total(questionTotal)
                .build());
        map.put("board", ResultDto.builder()
                .results(boards)
                .total(boardTotal)
                .build());
        map.put("gallery", ResultDto.builder()
                .results(galleries)
                .total(galleryTotal)
                .build());

        return map;
    }

    /**
     * 검색 조건으로 찾은 검색 결과 반환.
     * @param condition 검색 조건
     * @param table 검색할 테이블의 이름
     * @return 검색 결과 || null
     */
    public SearchResultDto getAllContentByCondition(SearchConditionDto condition, @NotNull String table){
        List<Map<String, Object>> results = null;
        Integer total = 0;
        List<CategoryDto> categories = this.getAllCategories(table);
        switch (table){
            case "notify":
                total = this.notifyMapper.notifiedContentsTotal(condition);
                if(total >0) results = this.notifyMapper.allNotifiedContents(condition);
                break;
            case "board" :
                total = this.boardMapper.allContentsTotal(condition, 0);
                if(total > 0) results = this.boardMapper.getAllContentByCondition(condition,0);
                break;
            case "gallery":
                total = this.boardMapper.allContentsTotal(condition, 1);
                if(total > 0) results = this.boardMapper.getGalleryContents(condition, 1);
                break;
            case "question":
                total = this.questionMapper.allQuestionsTotalCount(condition);
                if(total > 0) results = this.questionMapper.getAllQuestions(condition);
        }
        return SearchResultDto.builder()
                .results(results)
                .total(total)
                .categories(categories)
                .build();
    }

    public CommentDto getInsertedCommentInfo(CommentEntity comment){
        return this.commentMapper.getInsertedComment(comment);
    }

}
