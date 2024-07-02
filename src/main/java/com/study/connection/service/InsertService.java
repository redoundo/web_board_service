package com.study.connection.service;

import com.amazonaws.services.s3.AmazonS3;
import com.study.connection.dto.BoardSubmissionDto;
import com.study.connection.dto.QuestionSubmissionDto;
import com.study.connection.entity.CommentEntity;
import com.study.connection.entity.ImageEntity;
import com.study.connection.entity.NotifyEntity;
import com.study.connection.mapper.*;
import com.study.connection.utils.LoadFiles;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

/**
 * db 에 저장해주는 서비스
 */
@Service
@MultipartConfig
@RequiredArgsConstructor
@Lazy
public class InsertService {
    private final CommentMapper commentMapper;
    private final FileMapper fileMapper;
    private final AmazonS3 s3;

    private final NotifyMapper notifyMapper;
    private final QuestionMapper questionMapper;
    private final BoardMapper boardMapper; 
    /**
     * prop 을 db 에 저장한 뒤에 keyColumn , keyProperty 를 통해 contentId 를 받지 못했다면
     * 직접 찾아 파일 업로드를 한다.
     * @param dto db 에 저장할 내용.
     * @param files 업로드할 파일들.
     */
    @Transactional
    public Integer insertAllBoardContents(BoardSubmissionDto dto, List<MultipartFile> files){
        BoardSubmissionDto boardSubmissionDto = this.insertOnlyBoardContent(dto);
        this.insertOnlyFiles(files, boardSubmissionDto);
        return boardSubmissionDto.getBoardId();
    }

    /**
     * 게시글 저장 과정 
     * @param dto 저장할 내용
     * @return 게시글 저장으로 새롭게 생성된 boardId 가 들어 있는 게시글 내용
     */
    @Transactional
    public BoardSubmissionDto insertOnlyBoardContent(BoardSubmissionDto dto){
        this.boardMapper.insertNewContent(dto);
        if(dto.getBoardId() == null){
            Integer boardId = this.boardMapper.newBoardId(dto);
            dto.setBoardId(boardId);
        }
        return dto;
    }

    /**
     * 파일만 저장하는 과정
     * @param files 저장할 파일 내용
     * @param dto boardId, postTable 값을 가져오기 위해 필요한 게시글 내용
     */
    @Transactional
    public void insertOnlyFiles(List<MultipartFile> files, BoardSubmissionDto dto) {
        if(files == null || files.isEmpty()) return;

        List<MultipartFile> nonNullFiles = files.stream().filter(Objects::nonNull).toList();
        if(nonNullFiles.isEmpty()) return;

        List<ImageEntity> entityList = new LoadFiles(s3).upload(nonNullFiles, dto.getBoardId(), dto.getPostTable());
        this.fileMapper.insertFiles(entityList);
    }

    /**
     * 문의 게시글 생성.
     * @param dto 문의 게시글 내용
     * @return 문의 게시글의 아이디
     */
    public Integer insertQuestion(@NotNull QuestionSubmissionDto dto){
        this.questionMapper.insertNewQuestion(dto);
        return this.questionMapper.getInsertedQuestionId(dto);
    }

    /**
     * mybatis 에서 notifyId 를 돌려주지 않으면 직접 내용을 통해 notifyId 를 가져온다.
     * @param entity 생성할 공지 게시글 내용
     * @return 생성된 공지 게시글의 아이디
     */
    public Integer insertNotify(NotifyEntity entity){
        this.notifyMapper.insertNotification(entity);
        if(entity.getNotifyId() != null) return entity.getNotifyId();
        return this.notifyMapper.newNotificationId(entity);
    }

    /**
     * 댓글 내용 저장.
     * @param comment 저장할 댓글 내용.
     */
    public void insertComment(@NotNull CommentEntity comment){
        this.commentMapper.insertComment(comment);
    }
}
