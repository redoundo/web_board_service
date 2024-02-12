package com.study.connection.dao;

import com.study.connection.mapper.CategoryMapper;
import java.util.Map;

/**
 * categories db 처리
 */
public class CategoryDao {

    private final CategoryMapper mapper;
    public CategoryDao(CategoryMapper mapper){
        this.mapper = mapper;
    }

    public Map<Integer , String> selectCategories() throws Exception {
        Map<Integer , String> map = null;
        try{
            map = this.mapper.selectCategory();
        } catch (Exception e){
            throw  new Exception(e);
        }
        return map;
    }
    public String findCategoryNameByCategoryId(Integer id) throws Exception {
        String name = null;
        try{
            if(id != null){
                name = this.mapper.findCategoryName(id);
            }
        } catch (Exception e){
            throw new Exception(e);
        }
        return name;
    }
}

