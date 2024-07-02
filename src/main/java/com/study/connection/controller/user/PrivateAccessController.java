package com.study.connection.controller.user;

import com.amazonaws.services.s3.AmazonS3;
import com.study.connection.auth.AuthAnnotation;
import com.study.connection.dto.*;
import com.study.connection.dto.file.DownloadFileInfo;
import com.study.connection.entity.CommentEntity;
import com.study.connection.error.CustomRuntimeException;
import com.study.connection.error.ErrorCode;
import com.study.connection.service.DeleteService;
import com.study.connection.service.InsertService;
import com.study.connection.service.SelectService;
import com.study.connection.service.UpdateService;
import com.study.connection.utils.LoadFiles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 회원 인증이 필요한 요청 처리
 */
@RestController
@MultipartConfig
@RequiredArgsConstructor
@RequestMapping(value = {"/api/user"})
@PreAuthorize(value = "ROLE_USER")
@RolesAllowed(value = {"USER", "ROLE_USER"})
public class PrivateAccessController {
    private final InsertService insertService;
    private final UpdateService updateService;
    private final DeleteService deleteService;
    private final SelectService selectService;
    private final AmazonS3 s3;

    /**
     * 나의 문의 내역만 보기가 체크 되어져 있을 때 사용.
     * @param condition 검색 조건
     * @param userId 사용자 아이디.
     * @return 검색 결과
     */
    @GetMapping(value = {"/question"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SearchResultDto> getAllContents( @AuthAnnotation Integer userId,
                                                           @RequestParam SearchConditionDto condition){
 
        condition.setUserId(userId);
        SearchResultDto result = this.selectService.getAllContentByCondition(condition, "question");
        return ResponseEntity.ok(result);
    }
    
    /**
     * 자유 게시판 게시글 변경
     * @param id 변결할 게시글의 아이디
     * @param files 새로 업로드할 파일들.
     * @param dto 변경된 내용.
     * @return true || 예외
     */
    @PostMapping(value = {"/board/update/update"})
    public ResponseEntity<Boolean> updateBoardContent( @AuthAnnotation Integer userId,
                                                       @RequestParam Integer id,
                                                       List<MultipartFile> files,
                                                       @RequestBody BoardSubmissionDto dto){
        dto.setUserId(userId);
        dto.setBoardId(id);
        dto.setPostTable(0);

        this.updateService.updateBoardContent(dto, files, dto.getImages());
        return ResponseEntity.ok(true);
    }

    /**
     * 갤러리 게시판 게시글 변경
     * @param id 변결할 게시글의 아이디
     * @param files 새로 업로드할 파일들.
     * @param dto 변경된 내용.
     * @return true || 예외
     */
    @PostMapping(value = {"/gallery/update/update"})
    public ResponseEntity<Boolean> updateGalleryContent( @AuthAnnotation Integer userId,
                                                         @RequestParam Integer id,
                                                         List<MultipartFile> files,
                                                         @RequestBody BoardSubmissionDto dto){
        dto.setUserId(userId);
        dto.setBoardId(id);
        dto.setPostTable(1);

        this.updateService.updateBoardContent(dto, files, dto.getImages());
        return ResponseEntity.ok(true);
    }

    /**
     * 문의 게시판 게시글 변경
     * @param id 변경할 게시글 아이디
     * @param dto 변경한 내용.
     * @return true || 예외
     */
    @PostMapping(value = "/question/view/update")
    public ResponseEntity<Boolean> updateQuestionContent( @AuthAnnotation Integer userId,
                                                          @NotNull @NotBlank @RequestParam("contentId") Integer id,
                                                          @RequestBody QuestionSubmissionDto dto){
        dto.setUserId(userId);
        dto.setQuestionId(id);

        this.updateService.updateQuestion(dto);
        return ResponseEntity.ok(true);
    }



    /**
     * 댓글 내용 생성
     * @param userId 사용자 아이디
     * @param comment 생성할 댓글 내용
     * @return 저장 완료 여부
     */
    @PostMapping(value = {"/board/view/comment"})
    public ResponseEntity<CommentDto> insertViewComment( @AuthAnnotation Integer userId,
                                                         @NotNull @RequestBody CommentEntity comment){ 
        comment.setUserId(userId);
        
        this.insertService.insertComment(comment);
        CommentDto dto = this.selectService.getInsertedCommentInfo(comment);
        return ResponseEntity
                .ok()
                .body(dto);
    }

    /**
     * id 와 테이블을 받아 해당 게시글을 가져온다.
     * @param id 가져오려는 게시글의 id
     * @param table 게시글을 가져오려는 테이블의 이름
     * @return 게시글
     */
    @GetMapping(value = {"/{table}/update"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getContentByContentId( @AuthAnnotation Integer userId,
                                                                      @NotNull  @RequestParam("contentId") Integer id,
                                                                      @NotNull @NotBlank @PathVariable("table") String table ){ 
        Map<String, Object> result = this.selectService.getContentById(id, table);
        if(!table.equals("question")){
            List<CategoryDto> categories = this.selectService.getAllCategories(table);
            result.put("categories", categories);
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 게시물을 생성하기 전 카테고리 선택에 필요한 내용 제공.
     * @return 모든 카테고리가 담긴 리스트
     */
    @GetMapping(value = {"/{table}/write"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CategoryDto>> returnAllCategories( @NotNull @NotBlank @PathVariable(name = "table") String table){

        List<CategoryDto> categories =  this.selectService.getAllCategories(table);
        return ResponseEntity.ok(categories);
    }

    /**
     * 문의 게시글을 생성한 뒤 게시글의 아이디를 반환한다.
     * @param question 문의 게시글 내용
     * @param userId 사용자 아이디
     * @return 생성된 게시글의 아이디
     */
    @PostMapping(value = {"/question/write/insert"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> insertQuestion( @AuthAnnotation Integer userId,@RequestBody QuestionSubmissionDto question ){ 
        question.setUserId(userId);
        Integer questionId = this.insertService.insertQuestion(question);
        return ResponseEntity.ok(questionId);
    }

    /**
     * 자유 게시판 게시글 저장.
     * @param userId 사용자 아이디
//     * @param content 게시글 내용
//     * @param files 사용자가 업로드한 파일들.
     * @return 생성된 게시글 아이디
     */
    @PostMapping(value = {"/board/write/insert"})
    public ResponseEntity<Integer> insertBoardContent(@AuthAnnotation Integer userId,
                                                       BoardSubmissionDto content,
                                                       @RequestPart("files[]") List<MultipartFile> files){ 
        content.setUserId(userId);
        content.setPostTable(0);
        Integer boardId = this.insertService.insertAllBoardContents(content, files);

        return ResponseEntity.ok(boardId);
    }

    /**
     * 갤러리 게시글 저장.
     * @param userId 사용자 아이디
     * @param content 저장할 내용
     * @param "files" 저장할 파일들.
     * @return true || exceptions
     */
    @PostMapping(value="/gallery/write/insert" )
    public ResponseEntity<Boolean> insertGalleryContent( @AuthAnnotation Integer userId,
                                                         BoardSubmissionDto content
                                                         // , @RequestPart(name = "files[]") List<MultipartFile> files
    ){ 
        content.setUserId(userId);
        content.setPostTable(1);
        Integer boardId = this.insertService.insertAllBoardContents(content, content.getFiles());
        return ResponseEntity.ok(true);
    }



    /**
     * jwt 을 통해 userId 를 알아낸 뒤, comment 삭제 진행.
     * @param userId 사용자 아이디
     * @param id 삭제하려는 댓글이 위치한 게시글
     * @param comment 댓글 아이디
     * @return true || exception
     */
    @PostMapping(value = "/board/view/comment/delete")
    public ResponseEntity<Boolean> deleteComment( @AuthAnnotation Integer userId,
                                                  @NotNull @RequestParam("contentId") Integer id,
                                                  @RequestBody Map<String, Object> comment){ 

        this.deleteService.deleteAComment(id, userId, Integer.parseInt(comment.get("answerId").toString()));
        return ResponseEntity.ok(true);
    }

    /**
     *  게시글 삭제
     * @param userId 사용자 아이디
     * @param table db 테이블 이름
     * @param contentId 삭제할 게시글 아이디
     * @return true || exception
     */
    @GetMapping(value = {"/{table}/view/delete"})
    public ResponseEntity<Boolean> deleteContent( @AuthAnnotation Integer userId,
                                                  @NotNull @PathVariable String table,
                                                  @RequestParam Integer contentId){
        this.deleteService.deleteAContent(contentId, userId, table);
        return ResponseEntity.ok(true);
    }

    /**
     * 사용자 확인 뒤 다운로드 진행.
     * @param userId 사용자 아이디
     * @param fileInfo  view.vue 에서 파일 다운로드를 클릭하면 실행됨.
     * @return 파일을 다운할 수 있는 inputStream
     */
    @PostMapping(value = {"/gallery/update/download", "/board/update/download"})
    public ResponseEntity<ByteArrayResource> downloadFile(@AuthAnnotation Integer userId,
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
