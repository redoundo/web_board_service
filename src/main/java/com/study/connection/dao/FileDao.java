package com.study.connection.dao;

import com.study.connection.dto.FileDto;
import com.study.connection.entity.FileEntity;
import com.study.connection.mapper.FileMapper;

import java.util.List;


/**
 * 매개변수로 제공되는 내용을 검증하고 에러 발생시 controller 쪽으로 보낸다.
 * FilesMapper 를 이용해 db 처리를 하는 service 클래스
 */
public class FileDao {
    /**
     * file 처리용 mapper
     */
    private final FileMapper mapper;
    public FileDao(FileMapper mapper) {
        this.mapper = mapper;
    }
    /**
     * filesEntity 이용하여 db 저장
     * @param entity : controller 에서 받은 file Part 에서 추출된 정보로 null 이 없다.
     */
    public void insertFiles(List<FileEntity> entity) throws Exception {
        try{
            this.mapper.insertFile(entity);
        }catch (Exception e){
            throw new Exception(e);
        }
    }

    /**
     * contentId 로 포함되어져 있는 파일 내용들을 전부 지운다
     * 수정된 파일 내용들이 존재한다면 db 에 저장한다.
     * @param entities : restController 에서 받아온 file part 에서 나온 정보들. null 값 존재 X
     * @param id : 파일이 포함되어져 있는 내용의 id 값.
     * @throws Exception : SQLException 처리 할 예정.
     */
    public void updateFiles(List<FileEntity> entities , Integer id) throws Exception {
        try{
            this.mapper.deleteFilesByContentId(id);
            if(entities != null && !entities.isEmpty()){
                this.mapper.insertFile(entities);
            }
        }catch (Exception e){
            throw new Exception(e);
        }
    }
    /**
     *
     * @param id : content_id_have_file
     * @return : List<FilesDto>
     * @throws Exception : SQLException 또한 처리할 예정
     */
    public List<FileDto> selectFiles(Integer id) throws Exception {
        List<FileDto> dtos = null;
        try{
            dtos = this.mapper.selectFiles(id);
        } catch (Exception e){
            throw new Exception(e);
        }
        return dtos;
    }
    /**
     *
     * @param id : content_id_have_file
     * @throws Exception : SQLException 할 예정.
     */
    public void deleteFileById(Integer id) throws Exception {
        try{
            this.mapper.deleteFilesByContentId(id);
        }catch (Exception e){
            throw new Exception(e);
        }
    }
}
