package com.study.connection.mapper;

import com.study.connection.dto.*;
import com.study.connection.dto.content.SelectContentDto;
import com.study.connection.dto.content.UpdateContentDto;
import com.study.connection.entity.InsertContentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * contents 테이블 관련 mapper
 */
@Mapper
@Repository
public interface ContentMapper {
    /**
     * content 내용 삽입
     * @param entity
     */
     void insertContent(InsertContentEntity entity);
    /**
     * contentId = 매개변수 id 인 content 를 contents 테이블에서 삭제
     * @param id contentId
     */
    void deleteContent(Integer id);
    /**
     * contentId = 매개변수 id 인 내용을 업데이트 한다.
     * @param id contentId
     * @param entity 바꿀 내용들.
     */
    void updateContent(@Param("id") Integer id,@Param("entity") UpdateContentDto entity);
    /**
     * modify.html , view.html 같이 contentId를 통해 한개의 값만 가지고 올 때 사용.
     * @param id contentId
     * @return ContentDto
     */
    ContentDto selectContent(Integer id);
    /**
     * index.html 에서 사용된다. limit ?,10을 기본적으로 사용한다. 검색조건과 페이지 네이션을 고려한 값을 반환한다.
     * @param entity 검색 조건들.
     * @return List<SelectContentDto>
     */
    List<SelectContentDto> selectQueriedContents(ConditionDto entity);
    /**
     * selectQueriedContents 의 limit 조건 때문에 제공되지 못한 총 건수를 반환한다.
     * @param entity 검색 조건들.
     * @return 페이지 조건을 적용하지 않은 내용의 개수
     */
    Integer queriedTotal(ConditionDto entity);

    /**
     * 사용자가 입력한 비밀번호 유효성 판단을 위해 db 에서 password 를 가져온다.
     * @param id contentId
     * @return 해당 contentId 로 저장된 비밀번호.
     */
    String getPasswordByContentId(Integer id);

    /**
     * viewCount 값을 수정한다.
     * @param id 수정하려는 contentId
     * @param view viewCount 값
     */
    void updateViewCount(@Param("contentId") Integer id ,@Param("viewCount") Integer view);

    /**
     * 자동증가인 contentId 를 알 수 없으므로 저장했던 내용을 가지고 contentId 를 찾아서 온다.
     * @param forId contentId 를 찾기 위한 조건.
     * @return contentId
     */
    Integer getContentId(ThingsForGetContentId forId);
}
