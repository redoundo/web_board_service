package com.study.connection.controller.user;

import com.amazonaws.services.s3.AmazonS3;
import com.study.connection.dto.*;
import com.study.connection.dto.file.DownloadFileInfo;
import com.study.connection.error.CustomRuntimeException;
import com.study.connection.error.ErrorCode;
import com.study.connection.service.SelectService;
import com.study.connection.service.UpdateService;
import com.study.connection.utils.CheckValid;
import com.study.connection.utils.LoadFiles;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 로그인이 필요 없는 요청만 진행.
 */
@RestController
@MultipartConfig
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class PublicAccessController {
    private final UpdateService updateService;
    private final SelectService selectService;
    private final AmazonS3 s3;

    /**
     * view.vue 에서 파일 다운로드를 클릭하면 실행됨.
     * @return 파일을 다운할 수 있는 inputStream
     */
    @PostMapping(value = { "/board/view/download" })
    public ResponseEntity<ByteArrayResource> downloadFile( @NotNull @Valid @RequestBody DownloadFileInfo fileInfo){

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
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
        } catch (Exception e) {
            throw new CustomRuntimeException(ErrorCode.IOEXCEPTION_WHILE_DOWNLOAD_FILE);
        }
    }

    /**
     * 각 테이블 sample 내용을 모두 반환한다.
     * @param limits 각 게시판마다 설정된 페이지의 양
     * @return 내용 반환.
     */
    @PostMapping(value = "/main", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, ResultDto>> mainSamples( @RequestBody SampleLimits limits) {
        Map<String, ResultDto> result = this.selectService.limitedResultNoCondition(limits);
        return ResponseEntity.ok(result);
    }

    /**
     * id 와 테이블을 받아 해당 게시글을 가져온다.
     * @param id 가져오려는 게시글의 id
     * @param table 게시글을 가져오려는 테이블의 이름
     * @return 게시글
     */
    @GetMapping(value = {"/{table}/view"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getContentByContentId( @NotNull @NotBlank @RequestParam("contentId") String id,
                                                                      @NotNull @NotBlank @PathVariable("table") String table){

        Map<String, Object> result = this.selectService.getContentById(Integer.parseInt(id), table);
        // 조회수 업데이트
        this.updateService.updateViewCount(Integer.parseInt(id), table);
        return ResponseEntity.ok(result);
    }

    /**
     * 검색 조건을 설정한 검색 결과를 반환한다.
     * url 에 게시판의 내용이 들어있는 테이블의 이름을 넣어 다른 테이블과 구별할 수 있게 함.
     * @param condition 검색 조건
     * @param table 현재 게시판 이름
     * @return 검색 결과
     */
    @GetMapping(value = { "/{table}/search", "/{table}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SearchResultDto> getAllContents( @RequestParam Map<String, Object> condition,
                                                           @PathVariable("table") String table){
        SearchConditionDto dto = CheckValid.checking.notNullConditions(condition);
        SearchResultDto result = this.selectService.getAllContentByCondition(dto, table);
        return ResponseEntity.ok(result);
    }
}
