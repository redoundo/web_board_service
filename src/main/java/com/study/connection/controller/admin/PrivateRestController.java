package com.study.connection.controller.admin;

import com.amazonaws.services.s3.AmazonS3;
import com.study.connection.auth.AuthAnnotation;
import com.study.connection.auth.AuthService;
import com.study.connection.dto.CommentDto;
import com.study.connection.dto.DeleteCommentDto;
import com.study.connection.dto.DeleteContentDto;
import com.study.connection.dto.InsertComment;
import com.study.connection.dto.file.DownloadFileInfo;
import com.study.connection.entity.CommentEntity;
import com.study.connection.entity.UserEntity;
import com.study.connection.error.CustomRuntimeException;
import com.study.connection.error.ErrorCode;
import com.study.connection.service.*;
import com.study.connection.utils.LoadFiles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * thymeleaf 에서 formData 가 아닌 fetch api 요청일 때 호출되는 controller
 */
@RestController
@RolesAllowed(value = {"ADMIN", "ROLE_ADMIN"})
@PreAuthorize(value = "ROLE_ADMIN")
@RequiredArgsConstructor
@RequestMapping(value = "/api/admin")
@MultipartConfig
public class PrivateRestController {
    private final SelectService selectService;
    private final AuthService authService;
    private final DeleteService deleteService;
    private final InsertService insertService;
    private final CacheService cacheService;
    private final AmazonS3 s3;

    private final HttpSession session;
    private final SessionRegistry registry;

    /**
     *  로그아웃 진행
     * @param userId spring security 의 filter chain 들을 거치며 얻은 관리자 아이디
     * @return 로그아웃 여부 | exception
     */
    @GetMapping(value={"/logout"})
    public ResponseEntity<Boolean> logout( @AuthAnnotation Integer userId){ 
        registry.removeSessionInformation(session.getId());
        cacheService.deleteCache(session.getId());
        session.invalidate();
        return ResponseEntity.ok(true);
    }

    /**
     * 댓글 내용 생성
     * @param comment 생성할 댓글 내용
     * @return 저장 완료 여부
     */
    @PostMapping(value = {"/board/update/comment"})
    public ResponseEntity<CommentDto> insertViewComment( @AuthAnnotation Integer userId,
                                                         @RequestBody InsertComment comment){
        CommentEntity entity = CommentEntity.builder()
                .userId(userId)
                .postId(Integer.parseInt(comment.getPostId()))
                .answer(comment.getComment())
                .postTable(0)
                .build();
        this.insertService.insertComment(entity);
        CommentDto dto = this.selectService.getInsertedCommentInfo(entity);
        dto.setUserId(userId);
        return ResponseEntity
                .ok()
                .body(dto);
    }

    /**
     * board 게시글의 댓글 삭제
     * @param comment 삭제할 댓글의 정보
     * @return true || exception
     */
    @PostMapping(value = "/board/update/comment/delete")
    public ResponseEntity<DeleteCommentDto> deleteBoardComment( @AuthAnnotation Integer userId,
                                                                @RequestBody DeleteCommentDto comment){

        UserEntity user = this.authService.findUser(userId);
        this.deleteService.deleteAComment(Integer.parseInt(comment.getContentId()), user.getUserId(), comment.getAnswerId());

        return ResponseEntity.ok(comment);
    }

    /**
     *  게시글 삭제
     * @param userId 사용자 아이디
     * @param table db 테이블 이름
     * @param content 삭제할 게시글의 정보
     * @return true || exception
     */
    @PostMapping(value = {"/{table}/update/delete", "/{table}/answer/delete"})
    public ResponseEntity<Boolean> deleteContent( @AuthAnnotation Integer userId,
                                                  @RequestBody DeleteContentDto content,
                                                  @NotNull @PathVariable String table){
        if(content == null || content.getUserId() == null || content.getContentId() == null)
            throw new CustomRuntimeException(ErrorCode.MISSING_CONTENT_ERROR);

        this.deleteService.deleteAContent(Integer.parseInt(content.getContentId()), content.getUserId(), table);

        return ResponseEntity.ok(true);
    }


    /**
     *  관리자 확인 뒤 다운로드 진행.
     * @param fileInfo  view.vue 에서 파일 다운로드를 클릭하면 실행됨.
     * @return 파일을 다운할 수 있는 inputStream
     */
    @PostMapping(value = {"/gallery/update/download", "/board/update/download"})
    public ResponseEntity<ByteArrayResource> downloadFile( @AuthAnnotation Integer userId,
                                                           @NotNull @Valid @RequestBody DownloadFileInfo fileInfo){

        ByteArrayResource resource = new LoadFiles(s3).download(fileInfo.getFilePath(), fileInfo.getFileName());
        HttpHeaders header = new HttpHeaders();//httpHeader 설정.
        header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        header.set(HttpHeaders.CONTENT_DISPOSITION , "attachment; filename=\"" +
                URLEncoder.encode(fileInfo.getFileName(), StandardCharsets.UTF_8) + "\";");
        header.set(HttpHeaders.TRANSFER_ENCODING , "binary");

        try {
            return ResponseEntity
                    .ok()
                    .headers(header)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {
            throw new CustomRuntimeException(ErrorCode.IOEXCEPTION_WHILE_DOWNLOAD_FILE);
        }
    }
}
