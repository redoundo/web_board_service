package com.study.connection.mapper;

import com.study.connection.dto.CommentDto;
import com.study.connection.entity.CommentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * comments 테이블의 mapper
 */
@Mapper
@Repository
public interface CommentMapper {
    /**
     * comment 를 저장한다.
     * @param entity
     */
    void insertComment(CommentEntity entity);
    /**
     * commented_content_id = 매개변수 id 인 comment 들을 가져온다.
     * @param id
     * @return List<CommentDto>
     */
    List<CommentDto> selectComments(Integer id);

    /**
     * content 삭제시 해당 contentId로 존재하는 댓글을 삭제.
     * @param id
     */
    void deleteComments(Integer id);
}

