package com.study.connection.service;

import com.study.connection.dto.CommentDto;
import com.study.connection.entity.CommentEntity;
import com.study.connection.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.study.connection.utils.CheckValid.checking;

/**
 * controller 에서 준 매개변수들의 유효성 판단 및 mapper 로 값 받아오기
 */
@Service
public class CommentService {

    @Autowired
    private CommentMapper mapper;

    /**
     * @param entity 저장할 댓글 내용들.
     * @throws Exception
     */
    public void insertComment(CommentEntity entity) throws Exception {
        try{
            if(checking.checkClassMembers(entity)){
                this.mapper.insertComment(entity);
            } else{
                throw new Exception("댓글 저장을 위해서는 댓글 내용 전체가 필요합니다.");
            }
        }catch (Exception e){
            throw new Exception(e);
        }
    }

    /**
     *
     * @param id 댓글 찾는데 필요한 content_id
     * @return 해당 매개변수의 댓글 내용들.
     * @throws Exception
     */
    public List<CommentDto> getComments(String id) throws Exception {
        List<CommentDto> dtos = null;
        try{
            if(checking.checkString(id)){
                dtos = this.mapper.selectComments(Integer.parseInt(id));
            } else{
                throw new Exception("원하는 값을 찾아오기 위해서는 유효한 content_id 가 필요합니다.");
            }
        } catch (Exception e){
            throw new Exception(e);
        }
        return dtos;
    }
}
