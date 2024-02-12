package com.study.connection.dao;

import com.study.connection.dto.*;
import com.study.connection.entity.InsertContentEntity;
import com.study.connection.mapper.ContentMapper;
import java.util.List;

/**
 *
 */
public class ContentDao {

    private final ContentMapper mapper;
    public ContentDao(ContentMapper mapper){
        this.mapper = mapper;
    }

    public void insertContent(InsertContentEntity entity) throws Exception {
        try{
            this.mapper.insertContent(entity);
        }catch (Exception e){
            throw new Exception(e);
        }
    }
    public void updateContent(Integer id , UpdateContentDto dto) throws Exception {
        try{
            this.mapper.updateContent(id , dto);
        } catch (Exception e){
            throw new Exception(e);
        }
    }

    /**
     * @param id : 제거할 내용의 아이디 값.
     * @throws Exception : 매개변수가 null 혹은 유효하지 않거나 db 처리 중에 오류 발생시.
     * 오류가 발생하지 않으면 성공한 것으로 취급한다.
     */
    public void deleteContent(Integer id) throws Exception {
        try{
            this.mapper.deleteContent(id);
        }catch (Exception e){
            throw new Exception(e);
        }
    }

    /**
     *
     * @param dto
     * @return
     * @throws Exception
     */
    public Integer queriedContentTotal(ConditionDto dto) throws Exception {
        Integer total = 0;
        try{
            total = this.mapper.queriedTotal(dto);
        }catch (Exception e){
            throw new Exception(e);
        }
        return total;
    }

    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    public SelectContentDto selectContent(Integer id) throws Exception {
        SelectContentDto dto;
        try{
            dto = this.mapper.selectContent(id);
        }catch (Exception e){
            throw new Exception(e);
        }
        return dto;
    }

    /**
     *
     * @param dto
     * @return
     * @throws Exception
     */
    public List<SelectContentDto> queriedContents(ConditionDto dto) throws Exception {
        List<SelectContentDto> lists;
        try{
            lists = this.mapper.selectQueriedContents(dto);
        }catch (Exception e){
            throw new Exception(e);
        }
        return lists;
    }

    /**
     *
     * @param id 찾으려고 하는 내용의 아이디
     * @return 내용 아이디로 찾은 비밀번호 값.
     * @throws Exception
     */
    public String getPassword(Integer id) throws Exception {
        String password;
        try{
            password = this.mapper.getPasswordByContentId(id);
        } catch (Exception e) {
            throw new Exception(e);
        }
        return password;
    }

}
