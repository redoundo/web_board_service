package com.study.connection.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 *
 */
@Mapper
@Repository
public interface CategoryMapper {
    Map<Integer , String > selectCategory();
    String findCategoryName(Integer id);
}
