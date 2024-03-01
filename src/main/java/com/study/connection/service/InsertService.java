package com.study.connection.service;

import com.study.connection.dto.NotFileButInFiles;
import com.study.connection.dto.ThingsForGetContentId;
import com.study.connection.dto.WritePropsNeedInsert;
import com.study.connection.dto.content.UpdateContentDto;
import com.study.connection.dto.file.FileDto;
import com.study.connection.dto.file.FilePartDto;
import com.study.connection.entity.CommentEntity;
import com.study.connection.entity.FileEntity;
import com.study.connection.entity.InsertContentEntity;
import com.study.connection.error.CustomRuntimeException;
import com.study.connection.error.ErrorCode;
import com.study.connection.mapper.CommentMapper;
import com.study.connection.mapper.ContentMapper;
import com.study.connection.mapper.FileMapper;
import com.study.connection.utils.Encrypt;
import com.study.connection.utils.LoadFiles;
import com.study.connection.utils.NotNullInClass;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 내용 생성 서비스
 */
@Service
@RequiredArgsConstructor
@MultipartConfig
public class InsertService {
    private final ContentMapper contentMapper;
    private final CommentMapper commentMapper;
    private final FileMapper fileMapper;
    private final Logger logger = LoggerFactory.getLogger(InsertService.class);
    /**
     * 비밀번호 검증 후 해당 contentId 로 저장된 내용 전체 삭제.
     * @param contentId 삭제하려는 contentId
     * @param password 검증을 위해 필요한 사용자가 입력한 비밀번호.
     */
    public void deleteAllIfPasswordMatch(@NotNull @Min(1) Integer contentId ,
                                         @NotNull @NotBlank String password){

        String dbPassword = this.contentMapper.getPasswordByContentId(contentId);
        String encryptedPassword = new Encrypt().Encryption(password);
        if(!Objects.equals(dbPassword, encryptedPassword)){
            throw new CustomRuntimeException(ErrorCode.MISMATCH_PASSWORD_ERROR); // 비밀번호가 동일하지 않으면 에러를 던진다.
        } // 두 비밀번호가 동일할 경우 아래 내용 진행.

        this.commentMapper.deleteComments(contentId);//comments 테이블에 contentId 로 저장된 댓글 삭제.

        List<FileDto> files = this.fileMapper.selectFiles(contentId); //files 테이블에 존재하는 파일 내용 가져오기
        if(files != null && !files.isEmpty()) { // 삭제해야할 파일이 존재하면 실행.
            this.fileMapper.deleteFilesByContentId(contentId); // db 에 저장되어 있는 파일 내용 삭제.
            LoadFiles load = new LoadFiles();
            for(FileDto file : files){
                Boolean done = load.deleteFile(file.getFilePath()); // 물리적으로 저장된 파일을 삭제.
                if(!done){//삭제 안되면 로그를 찍고 계속 진행.
                    logger.debug("물리적으로 제거되지 못한 파일 내용  :   {}" , file);
                }
            }
        }

        this.contentMapper.deleteContent(contentId);// contents 테이블에 contentId 로 저장된 내용 삭제.
    }
    /**
     * 사용자가 입력한 비밀번호 검증 후 동일할 때 , files , contents 테이블 내용 수정. notFiles 에 없는 파일들은 삭제한 뒤 ,
     * fileParts 내용을 업로드 하고 거기서 가져온 내용을 데이터베이스에 저장.
     * @param fileParts 새로 생성해야하는 파일 내용들.
     * @param notFiles 유지해야하는 파일 내용들.
     * @param update 수정해야하는 게시물 내용.
     * @param contentId 수정하려는 게시물의 contentId
     */
    public void updateModifyProps(@Nullable FilePartDto fileParts , @Nullable NotFileButInFiles notFiles ,
                               @NotNull UpdateContentDto update , @NotNull @Min(1) Integer contentId) {

        String dbPassword = this.contentMapper.getPasswordByContentId(contentId);
        String encryptedPassword = new Encrypt().Encryption(update.getPassword());
        if(!Objects.equals(dbPassword, encryptedPassword)){
            throw new CustomRuntimeException(ErrorCode.MISMATCH_PASSWORD_ERROR);
        }

        if(notFiles != null){
            List<Object> notNulls = new NotNullInClass()
                    .getValuesByFieldName(notFiles , "fileId"); // 수정한 내용에서 존재하는 fileId들
            if(!notNulls.isEmpty()){//fileId가 하나라도 있을 때.
                List<Integer> fileIds = this.fileMapper.getFileIds(contentId);
                List<Integer> needDeleteIds = fileIds.stream().filter(f -> !notNulls.contains(f)).toList();
                if(!needDeleteIds.isEmpty()){ // 수정된 내용에 포함되지 않는 파일 id 가 존재할 때.
                    for(Integer id : needDeleteIds){
                        this.fileMapper.deleteByFileId(id); // 해당 fileId 를 db 에서 삭제한다.
                    }
                }
            }
        }// 수정된 내용에 포함되지 않지만 db 에는 포함되어 있는 파일 삭제 완료.

        if(fileParts != null){ // 업로드해야할 파일이 있을 때
            // null 이 아닌 multiPartFile 들
            List<MultipartFile> notNullFileParts =  new ArrayList<>(
                    Arrays.asList(fileParts.getFile1() , fileParts.getFile2() , fileParts.getFile3()))
                    .stream().filter(Objects::nonNull).toList();

            if(!notNullFileParts.isEmpty()){ // db 에 생성해야하는 파일이 하나라도 있을 때.
                List<FileEntity> entities = new LoadFiles().upload(notNullFileParts , contentId); // 업로드 진행 및 정보 추출.
                this.fileMapper.insertFile(entities);//db 에 저장.
            }
        }

        this.contentMapper.updateContent(contentId , update); // 수정한 내용 업데이트


    }

    /**
     * prop 을 db 에 저장한 뒤에 keyColumn , keyProperty 를 통해 contentId 를 받지 못했다면
     * 직접 찾아 파일 업로드를 한다.
     * @param prop db 에 저장할 내용.
     */
    public void insertContent(@NotNull WritePropsNeedInsert prop)  {
        try{


        InsertContentEntity content = prop.getContent();
        content.setPassword(new Encrypt().Encryption(content.getPassword()));//비밀번호에 단방향 암호화 적용.
        this.contentMapper.insertContent(content);//contents 테이블에 저장.

        if(prop.getFiles() != null){
            FilePartDto fileParts = prop.getFiles();
            List<MultipartFile> notNullFileParts = new ArrayList<>(
                    Arrays.asList(fileParts.getFile1() , fileParts.getFile2() , fileParts.getFile3()))
                    .stream().filter(Objects::nonNull).toList();

            if(!notNullFileParts.isEmpty()){
                // 내용 생성 후 mybatis 에서 contentId 를 제공하게 끔 설정을 했으나 혹시나 하는 마음에 내용을 직접 가제오게끔 만들었다.
                Integer id = content.getContentId() != null ?
                        content.getContentId()
                        : this.contentMapper.getContentId(
                                ThingsForGetContentId.builder()
                                        .contentCategoryId(content.getContentCategoryId())
                                        .content(content.getContent())
                                        .title(content.getTitle())
                                        .build());
                List<FileEntity> files = new LoadFiles().upload(notNullFileParts , id);
                this.fileMapper.insertFile(files);
            }
        }
        }catch (Exception e){
            logger.debug("INSERT SERVICE INSERT CONTENT ERROR  :  {}" , e.getMessage());
        }
    }

    /**
     * 댓글 내용 저장.
     * @param comment 저장할 댓글 내용.
     */
    public void insertComment(@NotNull CommentEntity comment){
        this.commentMapper.insertComment(comment);
    }
}
