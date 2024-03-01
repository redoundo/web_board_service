package com.study.connection.service;

import com.study.connection.dto.*;
import com.study.connection.dto.content.SelectContentDto;
import com.study.connection.dto.file.FileDto;
import com.study.connection.mapper.CategoryMapper;
import com.study.connection.mapper.CommentMapper;
import com.study.connection.mapper.ContentMapper;
import com.study.connection.mapper.FileMapper;
import com.study.connection.utils.Encrypt;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * contents , files , comments , categories
 */
@Service
@RequiredArgsConstructor
public class BoardService {
    private final ContentMapper contentMapper;
    private final CommentMapper commentMapper;
    private final FileMapper fileMapper;
    private final CategoryMapper categoryMapper;
    private final Logger logger = LoggerFactory.getLogger(BoardService.class);
    /**
     * 검색 조건을 적용해
     * @param condition 검색 조건
     * @return 검색 조건으로 가져온 내용과 전체 카티고리 , 검색 조건이 적용된 결과의 값.
     */
    public AllIndexPropsDto getIndexProps(ConditionDto condition){
        List<SelectContentDto> contents = this.contentMapper.selectQueriedContents(condition);
        Integer total = this.contentMapper.queriedTotal(condition);
        List<CategoryDto> categories = this.categoryMapper.allCategories();
        return AllIndexPropsDto.builder()
                .categories(categories)
                .total(total)
                .contents(contents)
                .build();
    }
    /**
     * contentId 를 통해 view.vue 에 필요한 내용을 가져온다.
     * @param contentId 내용을 가져올 contentId.
     * @return view.vue 에 필요한 내용.
     */
    public ViewPropsDto getViewProps(@NotNull @Min(1) Integer contentId){
        String categoryName = null;
        ContentDto content = null;
        List<FileDto> files = null;
        List<CommentDto> comments = null;
        try {
            content = this.contentMapper.selectContent(contentId);
            categoryName = this.categoryMapper.findCategoryName(content.getContentCategoryId());
            files = this.fileMapper.selectFiles(contentId);
            comments = this.commentMapper.selectComments(contentId);

            this.contentMapper.updateViewCount(contentId, content.getViewCount() + 1);


        }catch (Exception e){
            logger.debug(e.getMessage());
        }
        return ViewPropsDto.builder()
                .categoryName(categoryName)
                .comments(comments)
                .contents(content)
                .files(files)
                .build();
    }
    /**
     * contentId 로 존재하는 files , contents 테이블의 내용과 contentCategoryId 의 이름을 반환.
     * @param contentId 쿼리스트링이나 param 에서 가져온 contentId
     * @return view.vue 페이지에 필요한 내용 반환.
     */
    public ModifyPropsDto getModifyProps(@NotNull @Min(1) Integer contentId){
        ContentDto content = this.contentMapper.selectContent(contentId);
        String categoryName = this.categoryMapper.findCategoryName(content.getContentCategoryId());
        List<FileDto> files = this.fileMapper.selectFiles(contentId);
        return ModifyPropsDto.builder()
                .categoryName(categoryName)
                .files(files)
                .content(content)
                .build();
    }

    /**
     * write.vue 에서 카텔고리만 필요한 상태이므로 따로 제공.
     * @return db에 저장된 전체 카테고리.
     */
    public List<CategoryDto> getAllCategories(){
        return this.categoryMapper.allCategories();
    }

    /**
     *
     * @param things
     * @return
     */
    public Integer getContentIdByInserted(ThingsForGetContentId things){
        Integer id = null;
        try{
            id = this.contentMapper.getContentId(things);
        } catch (Exception e){
            logger.debug("BOARD SERVICE ERROR  getContentIdByInserted:   {}" , e.getMessage());
        }
        return id;
    }
    /**
     * 프론트에 넘겨진 비밀번호를 기반으로 사용자가 입력한 비밀번호가 동일한지 여부 확인.
     * @param password 사용자가 입력한 비밀번호
     * @param original 검색으로 가져와 프론트에 넘겨진 비밀번호.
     * @return 둘이 동일한지 여부
     */
    public Boolean checkPasswordBeforeDelete(@org.jetbrains.annotations.NotNull @NotBlank @NotEmpty String password ,
                                             @org.jetbrains.annotations.NotNull @NotBlank @NotEmpty String original){
        return new Encrypt().checkEncrypt(password , original);
    }
}
