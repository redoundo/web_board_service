package com.study.connection.mapper;

import com.study.connection.dto.QuestionSubmissionDto;
import com.study.connection.dto.SearchConditionDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 문의 게시판 관련 mapper
 */
@Mapper
@Repository
public interface QuestionMapper {
    /**
     * 검색 조건을 설정해 가져온 결과들을 반환한다.
     * @param condition 검색 조건
     * @return 검색 결과
     */
    List<Map<String, Object>> getAllQuestions(SearchConditionDto condition);
    /**
     * 검색 조건을 설정해 가져온 결과들의 개수를 반환한다.
     * @param condition 검색 조건
     * @return 검색 결과의 총 개수
     */
    Integer allQuestionsTotalCount(SearchConditionDto condition);

    /**
     * 전체 게시물 수
     * @return 게시물의 개수
     */
    Integer totalCount();
    /**
     * 최신 순으로 limit 만큼의 양만 반환.
     * @param limit 가져올 양
     * @return 내용이 들어있는 list
     */
    List<Map<String, Object>> limitedQuestions(Integer limit);
    /**
     * questionId 를 통해 해당 게시판의 내용을 가져온다.
     * @param id questionId
     * @return 게시판 내용
     */
    Map<String, Object> getQuestionByQuestionId(Integer id);

    /**
     * 문의 게시판 내용을 생성한다.
     * @param dto 생성할 게시판 내용
     */
    void insertNewQuestion(QuestionSubmissionDto dto);

    /**
     * 생성한 게시판의 내용으로 생성된 아이디를 가져온다.
     * @param dto 생성한 게시판 내용
     * @return questionId 게시판의 아이디
     */
    Integer getInsertedQuestionId(QuestionSubmissionDto dto);
    /**
     * 조회수를 변경한다.
     * @param id questionId 변경할 문의 게시판의 id
     */
    void updateViewCount(@Param("id")Integer id);

    /**
     * 문의 게시글 변경
     * @param dto 변경할 내용
     */
    void updateQuestion(QuestionSubmissionDto dto);

    /**
     * 게시글 삭제
     * @param contentId 삭제 하려는 게시글의 아이디
     * @param id 게시글을 작성한 사용자 아이디
     */
    void deleteQuestion(@Param("questionId") Integer contentId, @Param("userId") Integer id);
}
