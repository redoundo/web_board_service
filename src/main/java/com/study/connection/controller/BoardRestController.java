package com.study.connection.controller;

import com.study.connection.dto.*;
import com.study.connection.dto.content.UpdateContentDto;
import com.study.connection.dto.file.DownloadFileInfo;
import com.study.connection.dto.file.FilePartDto;
import com.study.connection.entity.CommentEntity;
import com.study.connection.error.CustomRuntimeException;
import com.study.connection.error.ErrorCode;
import com.study.connection.service.BoardService;
import com.study.connection.service.InsertService;
import com.study.connection.utils.LoadFiles;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 모든 요청 처리
 */
@RestController
@RequiredArgsConstructor
@MultipartConfig
public class BoardRestController {
    private final BoardService boardService;
    private final InsertService insertService;
    private final Logger logger = LoggerFactory.getLogger(BoardRestController.class);
    /**
     *  index.vue 에 필요한 모든 내용 제공.
     * @param condition 사용자가 설정한 검색 조건.
     * @return 검색 조건으로 나온 결과를 반환하는 responseEntity
     */
    @GetMapping(value = {"/index" , "" , "/"})
    public ResponseEntity<AllIndexPropsDto> getBoardContents(ConditionDto condition){
        Date end = Date.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("y-M-d")));
        Date start = Date.valueOf(LocalDateTime.now().minusYears(1).format(DateTimeFormatter.ofPattern("y-M-d")));

        ConditionDto conditionDto = ConditionDto.builder()
                .contentCategoryId(condition.getContentCategoryId() == null ? null : condition.getContentCategoryId())
                .page(condition.getPage() == null ? 1 : condition.getPage())
                .keyword(condition.getKeyword() == null ? null : condition.getKeyword())
                .end(condition.getEnd() == null? end: condition.getEnd())
                .start(condition.getStart() == null ? start : condition.getStart())
                .build();

        AllIndexPropsDto props = this.boardService.getIndexProps(conditionDto);
        return ResponseEntity
                .ok()
                .body(props);
    }
    /**
     * contentId 로 찾은 내용 반환.
     * 유효성 혹은 다른 runtimeException 들은 advice 클래스에서 다룬다.
     * @param contentId 가져올 내용의 아이디
     * @return content , files , comments 가 담긴 responseEntity 반환.
     */
    @GetMapping(value = {"/view"})
    public ResponseEntity<ViewPropsDto> returnViewProps(
            @NotNull @NotBlank @RequestParam(name="contentId") String contentId){
        ViewPropsDto prop = this.boardService.getViewProps(Integer.parseInt(contentId));
        return ResponseEntity
                .ok()
                .body(prop);
    }

    /**
     * contentId 로 modify.vue 에 필요한 내용 반환.
     * @param contentId 내용을 가져올 contentId
     * @return 내용을 담은 responseEntity
     */
    @GetMapping( value = {"/view/modify"})
    public ResponseEntity<ModifyPropsDto> getModifyProps(
            @NotNull @NotEmpty @RequestParam("contentId") String contentId){

        ModifyPropsDto props = this.boardService.getModifyProps(Integer.parseInt(contentId));
        return ResponseEntity
                .ok()
                .body(props);
    }

    /**
     * 게시물을 생성하기 전 카테고리 선택에 필요한 내용 제공.
     * @return 모든 카테고리가 담긴 리스트
     */
    @GetMapping(value = {"/write"})
    public ResponseEntity<List<CategoryDto>> returnAllCategories(){
        List<CategoryDto> categories =  this.boardService.getAllCategories();
        return ResponseEntity
                .ok()
                .body(categories);
    }

    /**
     * 사용자가 입력한 게시글 내용을 생성하고 해당 게시글의 아이디를 반환한다.
     * @param prop 새로 생성할 게시글 내용
     * @return 생성한 게시글의 contentId
     */
    @PostMapping(value = {"/write/insert"} ,
            headers = "Content-Type=multipart/form-data")
    public ResponseEntity<Integer> insertWriteContent(
            @Valid @RequestBody WritePropsNeedInsert prop) {
        Integer id = null;
        try{
            logger.debug("WritePropsNeedInsert prop :   {}" , prop);
        this.insertService.insertContent(prop);
        ThingsForGetContentId things = ThingsForGetContentId.builder()
                .title(prop.getContent().getTitle())
                .contentCategoryId(prop.getContent().getContentCategoryId())
                .content(prop.getContent().getContent())
                .build();
        id = this.boardService.getContentIdByInserted(things);
        } catch (Exception e){
            logger.debug("ERROR ON BOARD REST CONTROLLER  insertWriteContent :   {}" , e.getMessage());
        }
        return ResponseEntity
                .ok()
                .body(id);
    }
    /**
     * 예외가 던져지면 false 를 반환하게끔 진행.
     * @param parts 업로드가 필요한 파일들.
     * @param files 유지된 파일 내용.
     * @param update 변경된 contents 테이블의 내용
     * @param contentId 수정할 contentId
     * @return 수정 여부
     */
    @PostMapping(value = {"/view/modify/update"} ,
            headers = {"Content-Type=application/json" , "Content-Type=multipart/form-data"})
    public ResponseEntity<Boolean> updateModifyProps(
            @Nullable @RequestBody(required = false) FilePartDto parts ,
            @Nullable @RequestBody(required = false) NotFileButInFiles files ,
            @NotNull @Valid @RequestBody UpdateContentDto update ,
            @NotNull @NotEmpty @NotBlank @RequestParam("contentId") String contentId)  {

        this.insertService.updateModifyProps(parts , files , update , Integer.parseInt(contentId));
        return ResponseEntity
                .ok()
                .body(true);
    }

    /**
     * view.vue 에서 파일 다운로드를 클릭하면 실행됨.
     * @return 파일을 다운할 수 있는 inputStream
     */
    @PostMapping(value = {"/view/download"} ,headers = {"Content-Type=application/json"})
    public ResponseEntity<InputStreamResource> downloadFile(@NotNull @Valid @RequestBody DownloadFileInfo fileInfo){

        InputStreamResource resource = new LoadFiles().download(fileInfo.getFilePath());
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
                    .contentLength(resource.contentLength())
                    .body(resource);
        } catch (IOException e) {
            throw new CustomRuntimeException(ErrorCode.IOEXCEPTION_WHILE_DOWNLOAD_FILE);
        }
    }

    /**
     * 댓글 내용 생성
     * @param comment 생성할 댓글 내용
     * @return 저장 완료 여부 
     */
    @PostMapping(value = {"/view/comment"} ,headers = {"Content-Type=application/json"})
    public ResponseEntity<Boolean> insertViewComment(@NotNull @Valid @RequestBody CommentEntity comment){
        this.insertService.insertComment(comment);
        return ResponseEntity
                .ok()
                .body(true);
    }

    /**
     * 비밀번호가 동일하지 않거나 물리적인 파일이 삭제되지 않으면 예외가 발생하고 responseEntity 는 false 로 반환될 예정.
     * @param arg 게시글 삭제에 필요한 id 와 비밀번호
     * @return 예외가 발생하지 않았다면 true 가 반환되고 발생했다면 false 가 반환.
     */
    @PostMapping(value= {"/view/delete"} ,headers = {"Content-Type=application/json"})
    public ResponseEntity<Boolean> deleteIfHadValidPassword(
            @NotNull @Valid @RequestBody IdPasswordForDelete arg) {

        this.insertService.deleteAllIfPasswordMatch(arg.getContentId() , arg.getPassword());
        return ResponseEntity.ok().body(true);
    }

    /**
     * 사용자의 검색으로 가져와진 db 의 비밀번호와 사용자가 입력한 비밀번호가 동일한지 확인한다.
     * db 의 비밀번호가 위조되었을 가능성이 있지만 그것은 위의 컨트롤러에서 진행한다.
     * @param password 검색을 통해 db 에서 가져온 비밀번호 , 사용자가 입력한 비밀번호.
     * @return 동일 여부
     */
    @PostMapping(value = {"/view/delete/checkPassword"} ,headers = {"Content-Type=application/json"})
    public ResponseEntity<Boolean> checkPassword(@NotNull @RequestBody CheckPassword password){
        Boolean same = this.boardService.checkPasswordBeforeDelete(password.getPassword() , password.getOriginal());
        return ResponseEntity
                .ok()
                .body(same);
    }
}
