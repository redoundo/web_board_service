package com.study.connection.mapper;

import com.study.connection.dto.FileDto;
import com.study.connection.entity.FileEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * mybatis files 테이블 mapper
 */
@Mapper
@Repository
public interface FileMapper {
    /**
     * 여러 파일의 내용을 한번에 저장
     * @param entities 저장할 파일 내용 리스트
     */
    void insertFile(@Param("entities") List<FileEntity> entities);
    /**
     * content_id_have_file = 매개변수 id 경우, 전체 파일을 db 에서 지운다.
     * @param id 파일이 위치해 있는 content 의 contentId
     */
    void deleteFilesByContentId(Integer id);
    /**
     * contentIdHaveFile = 매개변수 id 인 모든 파일 중에 필요한 내용만 반환한다.
     * @param id 파일 내용들이 위치해 있을 내용의 아이디값 (contentId)
     * @return List<FilesDto>
     */
    List<FileDto> selectFiles(Integer id);

    /**
     * modify.html 에서 파일 내용을 수정할 때 전달된 file_id 로 삭제할 수 있는 기능 제공.
     * @param id file_id
     */
    void deleteByFileId(Integer id);
}
