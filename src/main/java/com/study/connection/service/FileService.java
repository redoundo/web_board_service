package com.study.connection.service;

import com.study.connection.dto.FileDto;
import com.study.connection.entity.FileEntity;
import com.study.connection.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.study.connection.utils.CheckValid.checking;

/**
 *
 */
@Service
public class FileService {
    @Autowired
    private FileMapper mapper;

    /**
     *
     * @param entities
     * @throws Exception
     */
    public void insertFiles(List<FileEntity> entities) throws Exception {
        try{
            if(entities != null && !entities.isEmpty()){
                for (FileEntity entity : entities){
                    if(!checking.checkClassMembers(entity)){
                        throw new Exception("파일 내용이 온전하지 않습니다.");
                    }
                }
                this.mapper.insertFile(entities);
            }
        }catch (Exception e){
            throw new Exception(e);
        }
    }

    /**
     * @param id contentId 값.
     * @param entities 수정할 파일 내용들 null 일 수도 비어있을 수도 아닐 수도 있다.
     * @throws Exception
     */
    public void updateFiles(String id , List<FileEntity> entities) throws Exception {
        try{
            if(checking.checkString(id)){
                if(entities != null && !entities.isEmpty()){
                    this.mapper.deleteFilesByContentId(Integer.parseInt(id));
                    this.mapper.insertFile(entities);
                }
            } else{
                throw new Exception("내용 아이디가 유효하지 않습니다.");
            }
        } catch (Exception e){
            throw new Exception(e);
        }
    }

    /**
     * @param id contentId 값.
     * @return
     * @throws Exception
     */
    public List<FileDto> getFiles(String id) throws Exception {
        List<FileDto> dtos;
        try{
            if(checking.checkString(id)){
                dtos = this.mapper.selectFiles(Integer.parseInt(id));
            } else{
                throw new Exception("내용 아이디가 유효하지 않습니다.");
            }
        }catch (Exception e){
            throw new Exception(e);
        }
        return dtos;
    }

    public void deleteByContentId(Integer id) throws Exception {
        try{
            if(id != null){
                this.mapper.deleteFilesByContentId(id);
            } else{
                throw  new Exception("파일을 지우기 위한 id 의 값이 null 입니다.");
            }
        }catch (Exception e){
            throw new Exception(e);
        }
    }

    /**
     * db 에서 저장된 파일 아이디들과 수정뒤에도 유지된 파일 아이디를 비교하여 유지된 파일 아이디 중에 없는 db 파일은 삭제한다.
     * @param contentId db 에 존재하는 파일들을 가져오기 위한 contentId
     * @param ids modify.html 에서 수정하면서 유지된 fileId 들
     * @throws Exception
     */
    public void deleteByFileId(String contentId, List<String> ids) throws Exception {
        try{
            if(checking.checkString(contentId)){
                //fileId 중 null 인 것 제외
                List<String> notNullFileIds = ids.stream().filter(id -> checking.checkString(id)).toList();
                if(!notNullFileIds.isEmpty()){
                    List<FileDto> files = this.mapper.selectFiles(Integer.parseInt(contentId));

                    for (FileDto file : files){
                        //수정 페이지에서 유지된 fileId 들 중에 db 에 존재하는 file_id 가 없을 경우. 삭제한다.
                        if(!notNullFileIds.contains(file.getFileId().toString())){
                            this.mapper.deleteByFileId(file.getFileId());
                        }//notNullFileIds 에 존재하는 fileId 들은 유지한다.
                    }
                }
            }
        }catch (Exception e){
            throw  new Exception(e);
        }
    }
}
