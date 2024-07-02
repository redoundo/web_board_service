package com.study.connection.mapper;

import com.study.connection.dto.CommentDto;
import com.study.connection.entity.CommentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
     * 특정 댓글을 삭제
     * @param contentId 댓글이 위치해 있는 게시글의 아이디
     * @param userId 댓글을 작성한 사용자의 아이디
     * @param commentId 댓글의 아이디
     */
    void deleteAComment(@Param("postId") Integer contentId, @Param("userId") Integer userId,
                        @Param("answerId") Integer commentId);

    /**
     * 댓글 내용으로 생성된 댓글의 정보를 가져온다.
     * @param entity 생성한 댓글의 내용
     * @return 댓글의 정보
     */
    CommentDto getInsertedComment(CommentEntity entity);
}

