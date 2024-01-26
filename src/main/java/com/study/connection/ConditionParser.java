package com.study.connection;

import com.study.connection.dto.SearchOptionDto;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.util.Map;

/**
 * 내용을 entity 들에 넣어 객체로 변환한다. dao 에서 이걸 사용한다.
 */
public class ConditionParser {

   public SearchOptionDto parse (@NotNull Map<String , String> params) {
       Integer categoryId = null;
       Integer contentId = null;
       int page = 1;

       String keyword = null;
       Date start = null;
       Date end = null;
       SearchOptionDto dto;
       if(params.get("content_id") != null && !params.get("content_id").isEmpty()) {
           contentId = Integer.parseInt(params.get("content_id"));
       }
       if(params.get("content_category_id") != null && !params.get("content_category_id").isEmpty() &&
               !params.get("content_category_id").equals("null")) {
           categoryId = Integer.parseInt(params.get("content_category_id"));
       }
       if(params.get("page") != null) {
           //페이지는 0 부터 시작하지만 , limit 는 1 부터 시작하기 때문에 page *10 +1
           page = Integer.parseInt(params.get("page")) * 10 + 1;
       }
       if(params.get("keyword") != null && !params.get("keyword").isEmpty()) {
           keyword = params.get("keyword");
       }
       if (params.get("start") != null && params.get("end") != null) {
           start = Date.valueOf(params.get("start"));
           end = Date.valueOf(params.get("end"));
           dto = SearchOptionDto.builder()
                   .categoryId(categoryId)
                   .contentId(contentId)
                   .end(end)
                   .start(start)
                   .page(page)
                   .build();
       } else if (params.get("start") == null && params.get("end") == null) {
           dto = SearchOptionDto.builder()
                   .keyword(keyword)
                   .contentId(contentId)
                   .categoryId(categoryId)
                   .page(page)
                   .build();
       } else {
           if(params.get("start") == null) {
               start = Date.valueOf(params.get("start"));
               dto = SearchOptionDto.builder()
                       .categoryId(categoryId)
                       .contentId(contentId)
                       .keyword(keyword)
                       .start(start)
                       .page(page)
                       .build();
           } else {
               end = Date.valueOf(params.get("end"));
               dto = SearchOptionDto.builder()
                       .categoryId(categoryId)
                       .contentId(contentId)
                       .keyword(keyword)
                       .end(end)
                       .page(page)
                       .build();
           }
       }
       return dto;
   }

   public String stringify (@NotNull SearchOptionDto dto) {
       //SearchOptionDto 를 Select Sql 문으로 조합
       String sql = "SELECT ? FROM contents WHERE submit_date BETWEEN STR_TO_DATE(?,'%Y-%m-%d %H:%i:%s') " +
               "AND STR_TO_DATE(?,'%Y-%m-%d %H:%i:%s') ";//start 와 end 는 이미 포함시킴.

       if(dto.getCategoryId() != null) {
           sql = sql + " AND content_category_id=?";
       }
       if(dto.getContentId() != null) {
           sql = sql + " AND content_id=?";
       }
       if(dto.getKeyword() != null) {
           sql = sql + " AND title LIKE '%?%'";
       }
       sql = sql + "  LIMIT ?,10;";

       return sql;
   }


}
