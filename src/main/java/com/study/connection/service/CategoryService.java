package com.study.connection.service;

import com.study.connection.dto.CategoryDto;
import com.study.connection.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.study.connection.utils.CheckValid.checking;

/**
 * controller 에서 넘어온 매개변수들의 유효성 판단과 mapper 로 값을 가져온다.
 */
@Service
public class CategoryService {
    @Autowired
    private CategoryMapper mapper;

    /**
     * @return 전체 카테고리를 반환한다.
     * @throws Exception
     */
    public List<CategoryDto> allCategories () throws Exception {
        List<CategoryDto> list;
        try{
            list = this.mapper.selectCategory();
        }catch (Exception e){
            throw new Exception(e);
        }
        return list;
    }

    /**
     * 특정 content_category_id 의 카테고리 이름을 가져온다.
     * @param id parseInt 할 때 null 오류가 날 수 있으므로 controller 가 아닌 service 에서 integral 로 변경
     * @return content_category_id 에 맞는 카테고리 이름을 반환.
     * @throws Exception db 처리 중에 오류가 발생했을 때, 문자열로 들어온 id가 null 일 떄 발생
     */
    public String findCategoryName(String id) throws Exception {
        String name = null;
        try{
            if(checking.checkString(id)){
                Integer intId = Integer.parseInt(id);
                name = this.mapper.findCategoryName(intId);
            }
        } catch (Exception e){
            throw  new Exception(e);
        }
        return name;
    }
}
