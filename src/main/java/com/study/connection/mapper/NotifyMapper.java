package com.study.connection.mapper;

import com.study.connection.dto.SearchConditionDto;
import com.study.connection.entity.NotifyEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 공지 게시판 mapper
 */
@Mapper
@Repository
public interface NotifyMapper {
    /**
     * 검색 조건을 설정해 찾은 결과들을 반환.
     * @param condition 검색 조건
     * @return 검색 결과들
     */
    List<Map<String, Object>> allNotifiedContents(SearchConditionDto condition);
    /**
     * 최신 순으로 limit 만큼의 양만 반환.
     * @param limit 가져올 양
     * @return 내용이 들어있는 list
     */
    List<Map<String, Object>> limitedNotification(Integer limit);
    /**
     * 검색 조건을 설정해 찾은 결과들의 개수를 반환.
     * @param condition 검색 조건
     * @return 검색 조건들의 개수
     */
    Integer notifiedContentsTotal(SearchConditionDto condition);
    /**
     * 전체 게시물 수
     * @return 게시물의 개수
     */
    Integer totalCount();
    /**
     * 공지 게시판의 아이디를 통해 게시판의 내용을 가져온다.
     * @param id notifyId
     * @return 공지 게시판 내용
     */
    Map<String, Object> getNotifyContentById(Integer id);

    /**
     * 문의 게시판 내용 생성.
     * @param entity 생성할 게시판의 내용
     */
    void insertNotification(NotifyEntity entity);
    Integer newNotificationId(NotifyEntity entity);
    /**
     * 조회수를 올린다.
     * @param id 변경할 게시판의 notifyId
     */
    void updateViewCount(@Param("id")Integer id);
    void updateNotification(NotifyEntity entity);
    /**
     * 공지사항 삭제
     * @param id notifyId
     */
    void deleteNotification(@Param("id")Integer id, @Param("adminId") Integer admin);
}
