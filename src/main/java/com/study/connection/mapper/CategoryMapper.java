package com.study.connection.mapper;

import com.study.connection.dto.CategoryDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 카테고리 mapper
 */
@Mapper
@Repository
public interface CategoryMapper {
    /**
     * 카테고리 아이디로 해당 카테고리 이름 가져오기
     * @param id 특정 카테고리 id
     * @return 해당 id 의 카테고리 이름
     */
    String findCategoryName(Integer id);

    /**
     * 모든 카테고리 내용 반환
     * @return 카테고리 내용
     */
    List<CategoryDto> allCategories();

    /**
     * 공지사항 카테고리 반환.
     * @return 공지사항 카테고리들
     */
    List<CategoryDto> getAllNotifyCategory();
    void insertNotifyCategory(String name);
    void insertBoardCategory(String name);
}
