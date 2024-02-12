package com.study.connection.service;

import com.study.connection.dto.*;
import com.study.connection.entity.FileEntity;
import com.study.connection.entity.InsertContentEntity;
import com.study.connection.mapper.ContentMapper;
import com.study.connection.utils.Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.study.connection.utils.CheckValid.checking;

/**
 * controller 에서 제공한 매개변수의 유효성 판단 및 mapper 를 통해 값 가져오기
 */
@Service
@Transactional
public class ContentService {
    @Autowired
    private ContentMapper mapper;

    /**
     * content 와 file 을 저장한다. fileService 에서 파일의 존재 여부를 확인해주므로 분가 없이 바로 접어넣는다.
     * @param entity 저장하려는 내용.
     * @param files 내용과 함께 저장하려고 하는 파일들.
     * @throws Exception
     */
    public void insertContent(InsertContentEntity entity , List<FileEntity> files) throws Exception {
        try{
            if(checking.checkClassMembers(entity)){
                this.mapper.insertContent(entity);
                new FileService().insertFiles(files);
            } else{
                throw new Exception("필수 내용 중 누락된 부분이 있습니다.");
            }
        }catch (Exception e){
            throw new Exception(e);
        }
    }

    /**
     * updateContentDto 에 포함된 password 와 db 의 password 가 동일한지 확인한 후에 내용 수정 진행.
     * @param id 수정할 contentId 값.
     * @param dto 수정할 내용
     * @param files 수정할 파일들.
     */
    public void updateContent(String id , UpdateContentDto dto , List<FileEntity> files)
            throws Exception {
       try{
           if(checking.checkString(id)){
               if(checking.checkClassMembers(dto)){
                   Integer intId = Integer.parseInt(id);
                   String original  = this.mapper.getPasswordByContentId(intId);//db 에 저장된 비밀번호
                   // 입력된 비밀번호와 db의 비밀번호가 동일한지 확인
                   if(new Encrypt().checkEncrypt(dto.getPassword() , original)){
                       this.mapper.updateContent(intId , dto); // content 저장
                       if(files != null){
                           new FileService().updateFiles(id , files); // 파일 저장
                       }
                   }
               } else{
                   throw  new Exception("수정하려는 내용이 온전하지 않습니다.");
               }
           }else{
               throw new Exception("수정하려는 내용의 아이디가 유효하지 않습니다.");
           }
       } catch (Exception e){
           throw new Exception(e);
       }
    }

    /**
     * 매개변수 id 값으로 존재하는 자세한 내용과 해당 id로 저장된 파일들과 댓글들을 반환한다.
     * @param id 가져오고 싶은 content 의 id 값.
     * @return 내용 , 파일들 , 댓글들을 포함한 객체
     */
    public ContentFullDetails contentCommentFiles(String id) throws Exception {
        ContentFullDetails details;
        try{
            if(checking.checkString(id)){
                ViewContentDto content = this.mapper.selectForView(Integer.parseInt(id));
                List<FileDto> files = new FileService().getFiles(id);
                List<CommentDto> comments = new CommentService().getComments(id);
                details = ContentFullDetails.builder()
                        .comments(comments)
                        .files(files)
                        .content(content)
                        .build();
            } else{
                throw new Exception("id 가 유효하지 않습니다.");
            }
        }catch (Exception e){
            throw new Exception(e);
        }
        return details;
    }

    /**
     * index.html 에 설정할 내용으로 조건으로 검색한 전체 내용과 카테고리 , 파일 존재 여부 등을 반환한다.
     * @param condition 검색 조건
     * @return 검색 조건과 페이지네이션이 반영된 내용들 , 검색 조건만 반영된 내용의 전체 갯수를 반환.
     * @throws Exception
     */
    public ContentTotalCategory queriedContentTotalCategory(ConditionDto condition) throws Exception {
        ContentTotalCategory queried;
        try{
            List<SelectContentDto> contents = this.mapper.selectQueriedContents(condition);
            Integer total = this.mapper.queriedTotal(condition);
            Map<Integer , String> categories = new CategoryService().allCategories();

            queried = ContentTotalCategory.builder()
                    .contents(contents)
                    .total(total)
                    .categories(categories)
                    .build();

        }catch (Exception e){
            throw new Exception(e);
        }
        return queried;
    }

    /**
     * db 의 비밀번호와 동일한지 확인한 뒤 , 해당 contentId 로 저장되어 있는 파일 , 댓글 을 삭제한 뒤 , 내용을 삭제한다.
     * @param id 삭제할 contentId 값
     * @param password 본인 확인을 위해 필요한 password 값.
     * @throws Exception
     */
    public void deleteContent(String id , String password) throws Exception {
        try{
            if(checking.checkString(id) && checking.checkString(password)){
                Integer intId = Integer.parseInt(id);
                String original = this.mapper.getPasswordByContentId(intId);//db 에 저장된 비밀번호를 가져온다.
                //저장된 비밀번호와 매개변수 password 가 동일한지 확인한다.
                if(new Encrypt().checkEncrypt(password , original)){
                    new FileService().deleteByContentId(intId);

                    this.mapper.deleteContent(intId);//contentId 값의 내용을 삭제한다.
                } else{
                    throw new Exception("비밀번호가 원래의 비밀번호와 다릅니다.");
                }
            } else{
                throw new Exception("내용 id 혹은 비밀번호가 유효하지 않습니다.");
            }
        }catch (Exception e){
            throw new Exception(e);
        }
    }
    public String getPasswordByContentId(String id) throws Exception {
        String password;
        try{
            if(checking.checkString(id)){
                password = this.mapper.getPasswordByContentId(Integer.parseInt(id));
            } else{
                throw new Exception("비밀번호가 유효하지 않습니다.");
            }
        }catch (Exception e){
            throw new Exception(e);
        }
        return password;
    }

    public void updateViewCount (Integer id , Integer view) throws Exception {
        try{
            if(id != null && view != null){
                this.mapper.updateViewCount(id , view);
            } else{
                throw new Exception("조회수 조정을 위한 id 가 유효하지 않습니다.");
            }
        }catch (Exception e){
            throw new Exception(e);
        }
    }

    /**
     * @param id 찾을 contentId 값
     * @return
     */
    public ContentFullDetails contentForModify(String id) throws Exception {
        ContentFullDetails dto;
        try{
            if(checking.checkString(id)){
               ViewContentDto view = this.mapper.selectForView(Integer.parseInt(id));
               List<FileDto> files = new FileService().getFiles(id);
               dto = ContentFullDetails.builder()
                       .files(files)
                       .content(view)
                       .build();
            }else {
                throw new Exception("유효하지 않은 contentId 입니다.");
            }
        }catch (Exception e){
            throw new Exception(e);
        }
        return dto;
    }

}
