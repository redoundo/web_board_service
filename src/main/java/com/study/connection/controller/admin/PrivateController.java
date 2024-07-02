package com.study.connection.controller.admin;

import com.study.connection.auth.AuthService;
import com.study.connection.dto.*;
import com.study.connection.entity.CommentEntity;
import com.study.connection.entity.NotifyEntity;
import com.study.connection.error.ErrorCode;
import com.study.connection.error.MvcRuntimeException;
import com.study.connection.service.*;
import com.study.connection.utils.CheckValid;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * thymeleaf 에서 formData 로 오는 내용들을 처리하는 controller
 */
@Controller
@RolesAllowed(value = {"ADMIN", "ROLE_ADMIN"})
@PreAuthorize(value = "ROLE_ADMIN")
@RequestMapping(value = "/admin")
@RequiredArgsConstructor
@MultipartConfig
public class PrivateController {
    private final SelectService selectService;
    private final UpdateService updateService;
    private final AuthService authService;
    private final InsertService insertService;

    /**
     * 로그인 한 후 redirect 되는 위치.
     * @param userDetails 관리자 정보
     */
    @GetMapping(value = {"/main"})
    public String mainPage( Model model, @AuthenticationPrincipal UserDetails userDetails){
        if(userDetails == null || userDetails.getUsername() == null)
            throw new MvcRuntimeException(ErrorCode.NEED_LOGIN_EXCEPTION, "admin/main", "/auth/login", model);
        if(!CheckValid.checking.checkGrantedAuthority(userDetails))
            throw new MvcRuntimeException(
                    ErrorCode.UNAUTHORIZED_ACCESS_EXCEPTION, "admin/gallery/update", "http://localhost:5000/main", model);
        String nickname = this.authService.findUserByUserId((int) Float.parseFloat(userDetails.getUsername()));
        model.addAttribute("nickname", nickname);

        return "admin/main";
    }

    /**
     * id 와 테이블을 받아 해당 게시글을 가져온다.
     * @param contentId 가져오려는 게시글의 id
     * @param table 게시글을 가져오려는 테이블의 이름
     * @param userDetails 관리자 정보
     * @return 게시글 위치
     */
    @GetMapping(value = {"/{table}/update/**", "/{table}/answer/**"})
    public String getContentByContentId( @NotNull @RequestParam Integer contentId,
                                         @NotNull @NotBlank @PathVariable("table") String table,
                                         @AuthenticationPrincipal UserDetails userDetails, Model model){

        if(userDetails == null || userDetails.getUsername() == null)
            throw new MvcRuntimeException(ErrorCode.NEED_LOGIN_EXCEPTION, "admin/" + table, "/auth/login", model);
        if(!CheckValid.checking.checkGrantedAuthority(userDetails))
            throw new MvcRuntimeException(
                    ErrorCode.UNAUTHORIZED_ACCESS_EXCEPTION, "admin/" + table, "http://localhost:5000/main", model);

        String nickname = this.authService.findUserByUserId((int) Float.parseFloat(userDetails.getUsername()));
        model.addAttribute("nickname", nickname);
        Map<String, Object> result = this.selectService.getContentById(contentId, table);
        model.addAttribute("results", result);
        
        this.updateService.updateViewCount(contentId, table); // 조회수 업데이트
        
        // 문의 게시글인 경우,  카테고리가 필요 없으므로 바로 return
        if(table.equals("question"))  return "admin/question/answer"; // question 게시글에는 카테고리가 존재하지 않으므로 패스

        List<CategoryDto> categories = this.selectService.getAllCategories(table);
        model.addAttribute("categories", categories);

        return "admin/" + table + "/update";
    }

    /**
     * 게시판 별 검섹
     * @param condition 검색 설정.
     * @param userDetails 관리자 정보
     * @param table 게시글을 가져오려는 테이블의 이름
     * @return 검색 결과가 저장된 위치
     */
    @GetMapping(value = {"/{table}/search/**", "/{table}/**"})
    public String indexSearch( Model model,
                               @RequestParam Map<String, Object> condition, @AuthenticationPrincipal UserDetails userDetails,
                               @NotNull @NotBlank @PathVariable String table){

        if(userDetails == null || userDetails.getUsername() == null)
            throw new MvcRuntimeException(ErrorCode.NEED_LOGIN_EXCEPTION, "admin/" + table, "/auth/login", model);
        if(!CheckValid.checking.checkGrantedAuthority(userDetails))
            throw new MvcRuntimeException(
                    ErrorCode.UNAUTHORIZED_ACCESS_EXCEPTION, "admin/" + table, "http://localhost:5000", model);
        String nickname = this.authService.findUserByUserId((int) Float.parseFloat(userDetails.getUsername()));
        SearchConditionDto dto = CheckValid.checking.notNullConditions(condition);
        SearchResultDto result = this.selectService.getAllContentByCondition(dto, table);
        model.addAttribute("contents", result.getResults());
        model.addAttribute("total", result.getTotal());
        model.addAttribute("nickname", nickname);

        if (table.equals("question")) return "admin/question";
        if(dto.getMaxCount() != null) model.addAttribute("maxCount", dto.getMaxCount());
        if(dto.getPage() != null) model.addAttribute("page", dto.getPage());

        model.addAttribute("categories", result.getCategories());
        return "admin/" + table;
    }

    /**
     * 자유 게시판 게시글 변경
     * @param files 새로 업로드할 파일들.
     * @param dto 변경된 내용.
     */
    @PostMapping(value = {"/board/update/update/**"})
    public String updateBoardContent( @AuthenticationPrincipal UserDetails userDetails,
                                      @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                      BoardSubmissionDto dto,
                                      Model model){

        if(userDetails == null || userDetails.getUsername() == null)
            throw new MvcRuntimeException(ErrorCode.NEED_LOGIN_EXCEPTION, "admin/board/update", "/auth/login", model);
        if(!CheckValid.checking.checkGrantedAuthority(userDetails))
            throw new MvcRuntimeException(
                    ErrorCode.UNAUTHORIZED_ACCESS_EXCEPTION, "admin/board/update", "http://localhost:5000/main", model);
        Integer userId = (int) Float.parseFloat(userDetails.getUsername());

        dto.setUserId(userId);
        dto.setPostTable(0);
        this.updateService.updateBoardContent(dto, files, dto.getImages());
        return "redirect:/admin/board";
    }
    /**
     * 갤러리 게시판 게시글 변경
     * @param files 새로 업로드할 파일들.
     * @param dto 변경된 내용.
     */
    @PostMapping(value = {"/gallery/update/update/**"})
    public String updateGalleryContent( @AuthenticationPrincipal UserDetails userDetails,
                                        @RequestPart(value = "files") List<MultipartFile> files,
                                        BoardSubmissionDto dto,
                                        Model model){

        if(userDetails == null || userDetails.getUsername() == null)
            throw new MvcRuntimeException(ErrorCode.NEED_LOGIN_EXCEPTION, "admin/gallery/update", "/auth/login", model);
        if(!CheckValid.checking.checkGrantedAuthority(userDetails))
            throw new MvcRuntimeException(
                    ErrorCode.UNAUTHORIZED_ACCESS_EXCEPTION, "admin/gallery/update", "http://localhost:5000/main", model);
        Integer userId = (int) Float.parseFloat(userDetails.getUsername());

        dto.setUserId(userId);
        dto.setPostTable(1);

        this.updateService.updateBoardContent(dto, files, dto.getImages());
        return "redirect:/admin/gallery";
    }

    /**
     * 문의 게시글 변경 및 답변 등록
     * @param userDetails 관리자 정보
     * @param dto 변경할 문의 게시글 내용
     */
    @PostMapping(value = "/question/answer/update/**")
    public String updateQuestionContent( @AuthenticationPrincipal UserDetails userDetails,
                                         @RequestBody QuestionSubmissionDto dto,
                                         Model model){

        if(userDetails == null || userDetails.getUsername() == null)
            throw new MvcRuntimeException(ErrorCode.NEED_LOGIN_EXCEPTION, "admin/gallery/update", "/auth/login", model);
        if(!CheckValid.checking.checkGrantedAuthority(userDetails))
            throw new MvcRuntimeException(
                    ErrorCode.UNAUTHORIZED_ACCESS_EXCEPTION, "admin/gallery/update", "http://localhost:5000/main", model);
        this.updateService.updateQuestion(dto);
        CommentEntity comment = CommentEntity.builder()
                .postTable(1)
                .postId(dto.getQuestionId())
                .answer(dto.getAnswer())
                .userId(dto.getUserId())
                .build();
        this.insertService.insertComment(comment);

        return  "redirect:/admin/question";
    }

    /**
     * 공지 게시글 내용 변경
     * @param userDetails 관리자 정보
     * @param entity 변경할 공지사항 게시글 내용
     * @return redirect 할 위치.
     */
    @PostMapping(value = "/notify/update/update/**")
    public String updateNotifyContent( @AuthenticationPrincipal UserDetails userDetails,
                                       NotifyEntity entity,
                                       Model model){

        if(userDetails == null || userDetails.getUsername() == null)
            throw new MvcRuntimeException(ErrorCode.NEED_LOGIN_EXCEPTION, "admin/notify/update", "/auth/login", model);
        if(!CheckValid.checking.checkGrantedAuthority(userDetails))
            throw new MvcRuntimeException(
                    ErrorCode.UNAUTHORIZED_ACCESS_EXCEPTION, "admin/notify/update", "http://localhost:5000/main", model);

        this.updateService.updateNotification(entity);

        return "redirect:/admin/notify";
    }

    /**
     * 공지사항 게시글 저장.
     * @param userDetails 관리자 정보
     * @param content 저장할 내용
     */
    @PostMapping(value = "/notify/write/insert/**")
    public String insertNotification( @AuthenticationPrincipal UserDetails userDetails,
                                      NotifyEntity content){

        Integer userId = (int) Float.parseFloat(userDetails.getUsername());
        content.setUserId(userId);

        if(content.getFixOnTop() == null) content.setFixOnTop(false);
        Integer notifyId = this.insertService.insertNotify(content);
        return "redirect:/admin/notify";
    }

    /**
     * 문의 게시글을 생성한 뒤 , redirect 시킨다.
     * @param userDetails 작성자 정보
     * @param question 문의 게시글 내용
     * @return 생성된 게시글의 아이디
     */
    @PostMapping(value = "/question/write/insert/**")
    public String insertQuestion( @AuthenticationPrincipal UserDetails userDetails,
                                  QuestionSubmissionDto question){

        Integer userId = (int) Float.parseFloat(userDetails.getUsername());
        question.setUserId(userId);

        Integer questionId = this.insertService.insertQuestion(question);
        return "redirect:/admin/question";
    }

    /**
     * 사용자가 입력한 내용을 통해 자유 게시판 혹은 갤러리 게시판의 게시글을 생성.
     * @param userDetails 작성자 정보
     * @param content 게시글 내용
     * @param files 사용자가 업로드한 파일들.
     * @return 생성된 게시글 아이디
     */
    @PostMapping(value = {"/board/write/insert/**"})
    public String insertBoardOrQuestion( @AuthenticationPrincipal UserDetails userDetails,
                                         BoardSubmissionDto content,
                                         @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                         Model model){

        if(userDetails == null || userDetails.getUsername() == null)
            throw new MvcRuntimeException(ErrorCode.NEED_LOGIN_EXCEPTION, "admin/board/write", "/auth/login", model);
        if(!CheckValid.checking.checkGrantedAuthority(userDetails))
            throw new MvcRuntimeException(
                    ErrorCode.UNAUTHORIZED_ACCESS_EXCEPTION, "admin/board/write", "http://localhost:5000/main", model);

        Integer userId = (int) Float.parseFloat(userDetails.getUsername());
        content.setUserId(userId);
        content.setPostTable(0);

        Integer boardId = this.insertService.insertAllBoardContents(content, files);
        return "redirect:/admin/board";
    }

    /**
     * 사용자가 입력한 내용을 통해 자유 게시판 혹은 갤러리 게시판의 게시글을 생성.
     * @param userDetails 작성자 정보
     * @param content 게시글 내용
     * @param files 사용자가 업로드한 파일들.
     * @return 생성된 게시글 아이디
     */
    @PostMapping(value = {"/gallery/write/insert/**"})
    public String insertGalleryContents( @AuthenticationPrincipal UserDetails userDetails,
                                         BoardSubmissionDto content,
                                         @RequestPart(value = "files") List<MultipartFile> files,
                                         Model model){

        if(userDetails == null || userDetails.getUsername() == null)
            throw new MvcRuntimeException(ErrorCode.NEED_LOGIN_EXCEPTION, "admin/gallery/write", "/auth/login", model);
        if(!CheckValid.checking.checkGrantedAuthority(userDetails))
            throw new MvcRuntimeException(
                    ErrorCode.UNAUTHORIZED_ACCESS_EXCEPTION, "admin/gallery/write", "http://localhost:5000/main", model);
        Integer userId = (int) Float.parseFloat(userDetails.getUsername());
        content.setUserId(userId);
        content.setPostTable(1);

        Integer boardId = this.insertService.insertAllBoardContents(content, files);
        return "redirect:/admin/gallery";
    }

    /**
     * 글 작성에 필요한 카테고리들을 반환.
     * @param userDetails 관리자 정보
     * @param table 게시글의 종류
     */
    @GetMapping(value = {"/{table}/write"})
    public String findCategories( @AuthenticationPrincipal UserDetails userDetails,
                                  Model model,
                                  @NotNull @NotBlank @PathVariable String table){

        if(userDetails == null || userDetails.getUsername() == null)
            throw new MvcRuntimeException(ErrorCode.NEED_LOGIN_EXCEPTION, "admin/gallery/update", "/auth/login", model);
        if(!CheckValid.checking.checkGrantedAuthority(userDetails))
            throw new MvcRuntimeException(
                    ErrorCode.UNAUTHORIZED_ACCESS_EXCEPTION, "admin/gallery/update", "http://localhost:5000/main", model);

        List<CategoryDto> categories = this.selectService.getAllCategories(table);
        model.addAttribute("categories", categories);
        return "admin/" + table + "/write";
    }
}