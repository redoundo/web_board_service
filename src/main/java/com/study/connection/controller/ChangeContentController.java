package com.study.connection.controller;

import com.study.connection.dto.ConditionDto;
import com.study.connection.dto.ErrorDto;
import com.study.connection.dto.FilePartDto;
import com.study.connection.entity.FileEntity;
import com.study.connection.entity.InsertContentEntity;
import com.study.connection.service.ContentService;
import com.study.connection.service.FileService;
import com.study.connection.utils.LoadFiles;
import com.study.connection.utils.QueryStringMapper;
import jakarta.servlet.annotation.MultipartConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
     *  내용 수정.
     * @param redirect /view.html
     * @param model modify.html
     * @param params modify.html 의 name 들.
     * @return redirect:/view.html || modify.html
     */
    @RequestMapping(value = "/view/modify/update" , method = RequestMethod.POST)
    public String updateContents(RedirectAttributes redirect , Model model , @RequestBody Map<String , Object> params){
        String to = "";
        try{
            List<String> justFileId = new ArrayList<>();//수정 후에도 유지된 file_id
            if(params.get("notFile1") != null){justFileId.add(params.get("notFile1").toString());}
            if(params.get("notFile2") != null){justFileId.add(params.get("notFile2").toString());}
            if(params.get("notFile3") != null){justFileId.add(params.get("notFile3").toString());}
            // 유지된 파일 아이디에 포함되지 않는 건 삭제
            new FileService().deleteByFileId(params.get("contentId").toString(), justFileId);

            FilePartDto files = new FilePartDto();//업로드할 파일들.
            if(params.get("file1") != null) {files.setFile1((MultipartFile) params.get("file1"));}
            if(params.get("file2") != null) {files.setFile2((MultipartFile) params.get("file2"));}
            if(params.get("file3") != null) {files.setFile3((MultipartFile) params.get("file3"));}
            new LoadFiles().upload(files , params.get("contentId").toString());//파일 업로드 진행.

            //리다이렉트 할 때를 위한 쿼리스트링 설정.
            new QueryStringMapper().redirectQueryMapping(redirect , params);

            to = "redirect:/view";

        }catch (Exception e){
            ErrorDto error = ErrorDto.builder()
                    .status(400)
                    .errorMessage(e.getMessage())
                    .build();
            model.addAttribute("error" , error);
            to = "modify";
        }
        return to;
    }
    /**
     * delete.html 에서 온 formAction 의 password 를 확인하고 db 에 저장된 password 와 동일하다면 삭제 진행한다.
     * @param contentId 삭제할 contentId
     * @param password 작성자 본인 확인을 위한 password
     * @param condition 쿼리스트링 조건
     * @param redirect 에러 미발생시 삭제 후 index.html 로 리다이렉트
     * @param model 에러 발생시 다시 시도하거나 동일하지 않을 때 오류 창을 띄우기 위해 delete.html 로 이동.
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

    /**
     * TODO: ModelAttribute 를 한번에 여러개 사용해도 되는지 확인해보자.
     * TODO : RedirectAttributes 와 Model 을 같이 써도 되는지 확인해보자.
     * @param redirect 리다이렉트 할 model
     * @param model write.html
     * @param dto 생성할 내용.
     * @param condition 쿼리스트링 처리를 위한 조건
     * @return redirect:/view || write
     */
    @RequestMapping(value = "/write" , method = RequestMethod.POST)
    public String insertContent(RedirectAttributes redirect  , Model model, @ModelAttribute InsertContentEntity dto ,
                                @ModelAttribute List<FileEntity> files,
                                @ModelAttribute ConditionDto condition){
        String to = "redirect:/view";
        try{
            this.service.insertContent(dto , files);
            if(condition != null){
                redirect.addAttribute("start" , condition.getStart());
                redirect.addAttribute("end" , condition.getEnd());
                redirect.addAttribute("keyword" , condition.getKeyword());
                redirect.addAttribute("contentCategoryId" , condition.getContentCategoryId());
                if(condition.getPage() != null){
                    redirect.addAttribute("page" , condition.getPage());
                }
            }
        }catch (Exception e){
            ErrorDto error = ErrorDto.builder()
                    .errorMessage("오류가  발생했습니다 조금 뒤에 다시 시도해주세요")
                    .status(400)
                    .build();
            model.addAttribute("error" , error);
            to = "write";
        }

        return to;
    }
}
