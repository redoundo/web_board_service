package com.study.connection.mapper;

import com.study.connection.dto.BoardContentDto;
import com.study.connection.dto.BoardSubmissionDto;
import com.study.connection.dto.SearchConditionDto;
import com.study.connection.dto.UpdateBoardContentDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 자유게시판 관련 mapper
 */
@Mapper
@Repository
public interface BoardMapper {
    /**
     * 검색 조건을 설정한 검색 결과 가져온다.
     * @param condition 검색 조건
     * @return 검색결과들
     */
    List<Map<String, Object>> getAllContentByCondition(@Param("condition") SearchConditionDto condition,@Param("table") Integer table);
    /**
     * 검색 조건을 설정한 검색 결과 가져온다.
     * @param condition 검색 조건
     * @return 검색결과들
     */
    List<Map<String, Object>> getGalleryContents(@Param("condition") SearchConditionDto condition,@Param("table") Integer table);
    /**
     * 검색 조건으로 가져온 결과들의 개수
     * @param condition 검색조건
     * @return 총 개수
     */
    Integer allContentsTotal(@Param("condition") SearchConditionDto condition,@Param("table") Integer table);

    List<Map<String, Object>> limitedBoard(@Param("limit") Integer limit, @Param("table") Integer table);

    List<Map<String, Object>> limitedGallery(@Param("limit") Integer limit, @Param("table") Integer table);
    /**
     * boardId 로 해당 게시판의 내용을 가져온다.
     * @param id boardId
     * @return 게시판 내용
     */
    Map<String, Object> getContentByBoardId(Integer id);
    /**
     * 전체 게시물 수
     * @param table 테이블 넘버
     * @return 게시물의 개수
     */
    Integer totalCount(Integer table);
    /**
     * 게시판 내용을 업데이트
     * @param update 업데이트할 내용
     */
    void updateBoardContent(UpdateBoardContentDto update);

    /**
     * file 을 commit 해주기 위해서는 file 을 업로드한 boardId 가 반드시 필요한데 boardContent 를 commit 하기 전에는
     * boardId 를 모르므로, db 에서 존재하는 가장 큰 boardId 값을 가져온다. file 에는 + 1 을 하여 commit 한다.
     * @return 현재 존재하는 bordId 중 가장 큰 값.
     */
    Integer biggestBoardId();

    /**
     * 게시판 조회시 조회수를 올린다.
     * @param id 변경할 게시판의 boardId
     */
    void updateViewCount(@Param("id") Integer id);
    /**
     * 게시글 생성.
     * @param dto 저장할 내용
     */
    void insertNewContent(BoardSubmissionDto dto);
    /**
     * 게시판 내용을 통해 생성한 게시판의 boardId를 가져온다.
     * @param content 게시판 내용
     * @return 게시판 boardId
     */
    Integer getInsertedBoardId(BoardContentDto content);
    Integer newBoardId(BoardSubmissionDto dto);

    /**
     * 게시글의 아이디와 사용자의 아이디로 해당 게시판을 지운다.
     * @param contentId 삭제할 게시글의 아이디
     * @param userId 삭제할 게시글을 적은 유저의 아이디
     */
    void deleteContentById(@Param("contentId") Integer contentId, @Param("userId") Integer userId);
}
