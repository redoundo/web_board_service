package com.study.connection.controller;

import com.study.connection.dto.*;
import com.study.connection.service.CategoryService;
import com.study.connection.service.ContentService;
import com.study.connection.utils.QueryStringMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.study.connection.utils.CheckValid.checking;

/**
 * index.html 에서 호출 시 기능을 제공하는 controller
 */
@Controller
public class GetContentController {
    @Autowired
    private ContentService service;
    private final Logger logger = LoggerFactory.getLogger(GetContentController.class);
    /**
     * 처음 렌더 후의 페이지 , 검색 후 결과 페이지 , 다른 곳에서 쿼리스트링을 포함한 채로 이동했을 때 호출됨.
     * @param model index.html
     * @param condition 검색 조건이 포함된 객체
     * @return index.html
     */
    @RequestMapping(value = { "/", "" , "/index.html"} , method = RequestMethod.GET)
    public String getContentsWithTotal(Model model , @ModelAttribute  ConditionDto condition){

        Date end = Date.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("y-M-d")));
        Date start = Date.valueOf(LocalDateTime.now().minusYears(1).format(DateTimeFormatter.ofPattern("y-M-d")));
        if(condition.getPage() == null){condition.setPage(1);}
        if(condition.getStart() == null){condition.setStart(start);}
        if(condition.getEnd() == null){condition.setEnd(end);}

        try{
            ContentTotalCategory all = this.service.queriedContentTotalCategory(condition);
            model.addAttribute("total" , all.getTotal());
            model.addAttribute("contents" , all.getContents());
            model.addAttribute("categories" , all.getCategories());

        } catch (Exception e){
            String uri = "/";//리다이렉트 할 위치
            String queries = new QueryStringMapper().mappingByCondition(condition);
            if(!queries.isEmpty()){
                uri = uri + "?" + queries; // 쿼리스트링이 존재하면 / 경로를 대체
            }

            ErrorDto error = ErrorDto.builder() // 에러 발생시 alert 를 실행한 뒤 / 로 redirect 한다.
                    .status(400)
                    .errorMessage(e.getMessage())
                    .redirectUri(uri)
                    .build();
            model.addAttribute("error" , error);
        }
        model.addAttribute("start" , condition.getStart());
        model.addAttribute("end" , condition.getEnd());
        model.addAttribute("keyword" , condition.getKeyword());
        model.addAttribute("contentCategoryId" , condition.getContentCategoryId());
        model.addAttribute("page" , condition.getPage());

        logger.debug("Model :    {}" , model);
        return "index";
    }
    /**
     * view.html 에서 필요한 내용을 제공하기 위해 쿼리스트링에 들어있는 contentId 를 가지고 content , files , comments 들을 찾아온다.
     * @param model view.html
     * @param id 검색하려는 contentId
     * @param condition 쿼리스트링
     * @return view.html
     */
    @RequestMapping(value = {"/view" , "/view.html"} , method = RequestMethod.GET)
    public String getViewContent(Model model , @RequestParam("contentId") String id
            , @ModelAttribute ConditionDto condition){
        try{
            if(checking.checkString(id)){

                model.addAttribute("commentedContentId" , id);
                ContentFullDetails details = service.contentCommentFiles(id);
                model.addAttribute("content" , details.getContent());
                model.addAttribute("files" , details.getFiles());
                model.addAttribute("comments" , details.getComments());

                //TODO : 만약 write.html , modify.html 에서 오는 경우에 viewCount 처리를 어떻게 할지 확실히 해야한다.
                service.updateViewCount(Integer.parseInt(id) , details.getContent().getViewCount() + 1);
            } else{
                throw new Exception("contentId가 존재하지 않습니다.");
            }
        }catch (Exception e){
            String uri = "/";
            if(condition != null){
                //view.html 에 진입하지 않는 이상 contentId 쿼리스트링은 존재할 이유가 없으므로 추가하지 않는다.
                uri = uri + "?" + new QueryStringMapper().mappingByCondition(condition);
            }
            ErrorDto error = ErrorDto.builder()
                    .errorMessage(e.getMessage())
                    .status(400)
                    .redirectUri(uri)
                    .build();
            model.addAttribute("error" , error);
        }
        return "view";
    }

    /**
     * 쿼리스트링에 포함되어져 있는 contentId 를 통해 해당 id 값을 가져오고 modify.html 에 설정한다.
     * @param model modify.html
     * @param id contentId
     * @return modify.html
     */
    @RequestMapping(value = "/view/modify" , method = RequestMethod.GET)
    public String getModifyContent(Model model , @RequestParam("contentId") String id ){
        try{
            if(checking.checkString(id)){
                ContentFullDetails details = this.service.contentForModify(id);
                String categoryName = new CategoryService().findCategoryName(id);
                model.addAttribute("content" , details.getContent());
                model.addAttribute("files" , details.getFiles());
                model.addAttribute("categoryName" , categoryName);
            } else{
                throw new Exception("contentId가 존재하지 않습니다.");
            }
        }catch (Exception e){
            ErrorDto error = ErrorDto.builder()
                    .errorMessage(e.getMessage())
                    .status(400)
                    .build();
            model.addAttribute("error" , error);
        }
        return "modfiy";
    }
}
