package com.study.connection.service;

import com.study.connection.dto.BoardSubmissionDto;
import com.study.connection.dto.QuestionSubmissionDto;
import com.study.connection.dto.UpdateBoardContentDto;
import com.study.connection.entity.ImageEntity;
import com.study.connection.entity.NotifyEntity;
import com.study.connection.mapper.*;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * db 변경 관련 서비스
 */
@Service
@MultipartConfig
@RequiredArgsConstructor
@Lazy
public class UpdateService {
    private final FileMapper fileMapper;

    private final NotifyMapper notifyMapper;
    private final QuestionMapper questionMapper;
    private final BoardMapper boardMapper;
    private final InsertService insertService;


    /**
     * 조회수 변경.
     * @param id 변경할 게시글의 아이디
     * @param tableName 변경할 테이블의 이름
     */
    public void updateViewCount(@NotNull Integer id , @NotNull String tableName){
        switch (tableName){
            case "notify":
                this.notifyMapper.updateViewCount(id);
                break;
            case "question":
                this.questionMapper.updateViewCount(id);
                break;
            case "board", "gallery":
                this.boardMapper.updateViewCount(id);
                break;
        }
    }
    /**
     * 수정한 내용들을 저장.
     * @param dto 수정한 내용
     * @param files 업로드 해야하는 파일
     * @param uploadedFiles 유지된 파일
     */
    @Transactional
    public void updateBoardContent(BoardSubmissionDto dto, List<MultipartFile> files, List<Integer> uploadedFiles){
        this.updateOnlyBoardContent(dto);
        this.insertService.insertOnlyFiles(files, dto);
        this.updateFilesByFiltering(dto, uploadedFiles);
    }

    /**
     * 게시글 업데이트 과정
     * @param dto 업데이트 할 게시글의 내용
     */
    @Transactional
    public void updateOnlyBoardContent(BoardSubmissionDto dto){
        UpdateBoardContentDto updateBoardContentDto = UpdateBoardContentDto.builder()
                .boardId(dto.getBoardId())
                .content(dto.getContent())
                .title(dto.getTitle())
                .categoryId(dto.getCategoryId())
                .build();
        this.boardMapper.updateBoardContent(updateBoardContentDto);
    }

    /**
     * 게시글 변경시 유지되지 못한 파일들을 전부 삭제하는 과정
     * @param dto boardId를 얻기 위한 BoardSubmissionDto 객체
     * @param uploadedFiles 유지된 파일들의 아이디
     */
    @Transactional
    public void updateFilesByFiltering(BoardSubmissionDto dto, List<Integer> uploadedFiles){
        List<ImageEntity> uploaded = this.fileMapper.existFileByPostId(dto.getBoardId()); // db 에 존재하는 파일 가져옴.
        // 유지된 파일들의 id 를 분리
        List<Integer> uploadedFileImageId = new ArrayList<>(uploadedFiles);
        List<ImageEntity> maintainedFile = uploaded.stream()
                .filter(file -> !uploadedFileImageId.contains(file.getImageId())).toList(); // 사용자가 유지시킨 id 안에 있는 것만 포함.

        this.fileMapper.deleteFilesByContentId(dto.getBoardId()); // 파일 전부 삭제
        if(!maintainedFile.isEmpty()) this.fileMapper.insertFiles(maintainedFile); // 유지된 파일 다시 저장.

    }

    public void updateQuestion(QuestionSubmissionDto dto){
        this.questionMapper.updateQuestion(dto);
    }
    public void updateNotification(NotifyEntity entity){
        this.notifyMapper.updateNotification(entity);
    }
}
