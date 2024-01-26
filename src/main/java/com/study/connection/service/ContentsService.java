package com.study.connection.service;

import com.study.connection.dao.ContentDao;
import com.study.connection.dto.ContentDto;
import com.study.connection.dto.SearchOptionDto;
import com.study.connection.entity.ContentEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ContentsService {

    //컨텐츠 내용 삽입하기
    public void insert (@NotNull ContentDto dto) throws Exception{

        if(dto.getPassword() != null && dto.getNickname() != null && dto.getContent() != null &&
                dto.getContentCategoryId() != null && dto.getTitle() != null) {

            try {
                new ContentDao().insert(dto);
            } catch (Exception e) {
                //처리가 되지 않았음을 알리기 위해 controller 로 에러를 던진다.
                throw new Exception(e);
            }

        } else {
            throw new Exception("ContentsService_InsertError :   required dto member is null!");
        }
    }

    //내용 변경하기
    public void update (@NotNull ContentDto dto) throws  Exception {

        if(dto.getTitle() != null && dto.getContentId() != null && dto.getContent() != null && dto.getNickname() != null) {

            try {
                new ContentDao().modify(dto);
            } catch (Exception e) {
                //처리가 되지 않았음을 알리기 위해 controller 로 에러를 던진다.
                throw new Exception(e);
            }

        } else {
            throw new Exception("ContentsService_modifyError :   required dto member is null!");
        }
    }

    public void delete(@NotNull String contentId) throws Exception {
        if(!contentId.isEmpty()) {
            try {
                //numberFormatException 도 예외처리하게끔 string 형으로 넘겨받음.
                Integer id = Integer.parseInt(contentId);
                new ContentDao().delete(id);
            }catch (Exception e) {
                //처리가 되지 않았음을 알리기 위해 controller 로 에러를 던진다.
                throw new Exception(e);
            }
        }
    }
}
