package com.study.connection.mapper;

import com.study.connection.dto.*;
import com.study.connection.entity.InsertContentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ContentMapper {
    /**
     * content 내용 삽입
     * @param entity
     */
    void insertContent(InsertContentEntity entity);
    /**
     * content_id = 매개변수 id 인 content 를 삭제
     * 해당 content_id 를 가진 files , comments 들을 삭제하는 과정도 포함해야한다.
     * @param id
     */
    void deleteContent(Integer id);
    /**
     * content_id = 매개변수 id 인 내용을 업데이트 한다.
     * @param id
     * @param entity
     */
    void updateContent(@Param("id") Integer id,@Param("entity") UpdateContentDto entity);
    /**
     * modify.jsp , view.jsp 같이 content_id를 통해 한개의 값만 가지고 올 때 사용.
     * @param id
     * @return SelectContentDto
     */
    SelectContentDto selectContent(Integer id);
    /**
     * board.jsp 에서 사용된다. limit ?,10을 기본적으로 사용한다. 검새조건과 페이지 네이션을 고려한 값을 반환한다.
     * @param entity
     * @return List<SelectContentDto>
     */
    List<SelectContentDto> selectQueriedContents(ConditionDto entity);
    /**
     * selectQueriedContents 의 limit 조건 때문에 제공되지 못한 총 건수를 반환한다.
     * @param entity
     * @return Integer
     */
    Integer queriedTotal(ConditionDto entity);

    /**
     * 사용자가 입력한 비밀번호 유효성 판단을 위해 db 에서 password 를 가져온다.
     * @param id
     * @return
     */
    String getPasswordByContentId(Integer id);

    /**
     * view_count 값을 수정한다.
     * @param id 수정하려는 contentId
     * @param view viewCount 값
     */
    void updateViewCount(@Param("contentId") Integer id ,@Param("viewCount") Integer view);

    /**
     * fileExistence 제외 password 포함.
     * @param id contentId
     * @return password 를 포함한 contents 의 내용.
     */
    ViewContentDto selectForView(Integer id);
}
