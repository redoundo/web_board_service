package com.study.connection.controller;

import com.study.connection.dto.ConditionDto;
import com.study.connection.dto.ErrorDto;
import com.study.connection.dto.FilePartDto;
import com.study.connection.dto.InsertContent;
import com.study.connection.entity.FileEntity;
import com.study.connection.service.ContentService;
import com.study.connection.service.FileService;
import com.study.connection.utils.Encrypt;
import com.study.connection.utils.LoadFiles;
import com.study.connection.utils.QueryStringMapper;
import jakarta.servlet.annotation.MultipartConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.study.connection.utils.CheckValid.checking;

/**
 * contents 테이블의 수정 , 추가 , 삭제 기능 모음.
 */

@Controller
@MultipartConfig
public class ChangeContentController {
    @Autowired
    private ContentService service;

    /**
     * 내용 수정.
     *
     * @param redirect /view.html
     * @param model    modify.html
     * @param params   modify.html 의 name 들.
     * @return redirect:/view.html || modify.html
     */
    @RequestMapping(value = "/view/modify/update", method = RequestMethod.POST)
    public String updateContents(RedirectAttributes redirect, Model model, @RequestBody Map<String, Object> params) {
        String to;
        try {
            List<String> justFileId = new ArrayList<>();//수정 후에도 유지된 file_id
            if (params.get("notFile1") != null) {
                justFileId.add(params.get("notFile1").toString());
            }
            if (params.get("notFile2") != null) {
                justFileId.add(params.get("notFile2").toString());
            }
            if (params.get("notFile3") != null) {
                justFileId.add(params.get("notFile3").toString());
            }
            // 유지된 파일 아이디에 포함되지 않는 건 삭제
            new FileService().deleteByFileId(params.get("contentId").toString(), justFileId);

            FilePartDto files = new FilePartDto();//업로드할 파일들.
            if (params.get("file1") != null) {
                files.setFile1((MultipartFile) params.get("file1"));
            }
            if (params.get("file2") != null) {
                files.setFile2((MultipartFile) params.get("file2"));
            }
            if (params.get("file3") != null) {
                files.setFile3((MultipartFile) params.get("file3"));
            }
            List<FileEntity> fileEntities = new LoadFiles().upload(files, params.get("contentId").toString());//파일 업로드 진행.
            new FileService().updateFiles(params.get("contentId").toString(), fileEntities);
            //리다이렉트 할 때를 위한 쿼리스트링 설정.
            new QueryStringMapper().redirectQueryMapping(redirect, params);

            to = "redirect:/view";

        } catch (Exception e) {
            ErrorDto error = ErrorDto.builder()
                    .status(400)
                    .errorMessage(e.getMessage())
                    .build();
            model.addAttribute("error", error);
            to = "modify";
        }
        return to;
    }

    /**
     * delete.html 에서 온 formAction 의 password 를 확인하고 db 에 저장된 password 와 동일하다면 삭제 진행한다.
     *
     * @param contentId 삭제할 contentId
     * @param password  작성자 본인 확인을 위한 password
     * @param condition 쿼리스트링 조건
     * @param redirect  에러 미발생시 삭제 후 index.html 로 리다이렉트
     * @param model     에러 발생시 다시 시도하거나 동일하지 않을 때 오류 창을 띄우기 위해 delete.html 로 이동.
     * @return index.html || delete.html
     */
    @RequestMapping(value = "/view/delete", method = RequestMethod.POST)
    public String deleteContent(@RequestParam("commentedContentId") String contentId,
                                @RequestParam("password") String password,
                                @ModelAttribute ConditionDto condition,
                                RedirectAttributes redirect,
                                Model model) {
        String to;
        try {
            if (checking.checkString(contentId)) {
                //contentId 로 찾은 db 의 비밀번호와 입력한 비밀번호가 동일하다면 삭제하는 과정 내장.
                this.service.deleteContent(contentId, password);

                if (condition != null) {//redirect 했을 때의 쿼리스트링 설정.
                    if (condition.getEnd() != null || condition.getStart() != null) {//검색을 했을 때
                        redirect.addAttribute("start", condition.getStart());
                        redirect.addAttribute("end", condition.getEnd());
                        redirect.addAttribute("keyword", condition.getKeyword());
                        redirect.addAttribute("contentCategoryId", condition.getContentCategoryId());
                        redirect.addAttribute("page", condition.getPage());
                    } // else : 검색을 안했을 때.
                }
                to = "redirect:/index";
            } else {
                throw new Exception("contentId 가 유효하지 않습니다.");
            }
        } catch (Exception e) {
            ErrorDto error = ErrorDto.builder()
                    .errorMessage(e.getMessage())
                    .status(400)
                    .build();
            model.addAttribute("error", error);
            //TODO : 원래의 값이 유지되는지 여부 확인이 필요하다.
            //TODO : view.html 을 반환했을 때와 delete.html 을 반환했을 때 무슨 차이가 있는지 확인해보자.
            to = "delete";
        }
        return to;
    }

    @RequestMapping(value = "/insertContent", method = RequestMethod.POST)
    public String insertingContent(RedirectAttributes redirect, @RequestParam Map<String , Object> params
            ,@ModelAttribute FilePartDto fileDto) {
        String to = "redirect:/view";
        try {
            InsertContent insertEntity = InsertContent.builder()
                    .password(new Encrypt().Encryption(params.get("password").toString()))
                    .submitDate(Date.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("y-M-d"))))
                    .viewCount(0)
                    .content(params.get("content").toString())
                    .contentCategoryId(Integer.parseInt(params.get("contentCategoryId").toString()))
                    .title(params.get("title").toString())
                    .nickname(params.get("nickname").toString())
                    .build();
            this.service.insertContent(insertEntity , fileDto); // 내용 저장.

            QueryStringMapper queryMapper = new QueryStringMapper();
            queryMapper.redirectQueryMapping(redirect , queryMapper.toQueryStringMap(params.get("currentUrl").toString()));
        } catch (Exception e) {
            e.getStackTrace();
            System.out.println(e.getMessage());
        }
        return to;
    }
}
