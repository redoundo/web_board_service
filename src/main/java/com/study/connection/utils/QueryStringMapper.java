package com.study.connection.utils;

import com.study.connection.dto.ConditionDto;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.study.connection.utils.CheckValid.checking;

/**
 *
 */
public class QueryStringMapper {
    /**
     * @param dto 쿼리스트링 내용
     * @return  null 이 아닌 값들만 있는 쿼리스트링.
     */
    public String mappingByCondition(@NotNull ConditionDto dto){
        List<String> queries = new ArrayList<>();
        if(dto.getStart() != null){ queries.add("start=" + dto.getStart()); }
        if(dto.getEnd() != null) { queries.add("end=" + dto.getEnd());}
        if(dto.getPage() != null){ queries.add("page=" + dto.getPage());}
        if(dto.getKeyword() != null){ queries.add("keyword=" + dto.getKeyword());}
        if(dto.getContentCategoryId() != null){queries.add("contentCategoryId=" + dto.getContentCategoryId());}

        return  String.join("&" , queries);
    }

    public RedirectAttributes redirectQueryMapping(RedirectAttributes redirect ,@NotNull ConditionDto dto){

        if(dto.getPage() != null){redirect.addAttribute("page" , dto.getPage());}
        if(dto.getContentCategoryId() != null){redirect.addAttribute("contentCategoryId" , dto.getContentCategoryId());}
        if(dto.getKeyword() != null){redirect.addAttribute("contentCategoryId" , dto.getContentCategoryId());}
        if(dto.getStart() != null){redirect.addAttribute("start" , dto.getStart());}
        if(dto.getEnd() != null){redirect.addAttribute("end" , dto.getEnd());}

        return redirect;
    }
    public RedirectAttributes redirectQueryMapping(RedirectAttributes redirect , @NotNull Map<String , Object> param){

        if(param.get("page") != null){redirect.addAttribute("page" , param.get("page"));}
        if(param.get("end") != null){redirect.addAttribute("end" , param.get("end"));}
        if(param.get("start") != null){redirect.addAttribute("start" , param.get("start"));}
        if(param.get("keyword") != null){redirect.addAttribute("keyword" , param.get("keyword"));}
        if(param.get("contentCategoryId") != null){redirect.addAttribute("contentCategoryId" , param.get("contentCategoryId"));}

        return redirect;
    }
    public Map<String ,Object> toQueryStringMap(String url){
        Map<String , Object> map = new HashMap<>();
        if(checking.checkString(url)){
            if(url.contains("?") && url.contains("=")){
                String query = url.split("\\?" , 1)[1];
                if(url.contains("&")){
                    //쿼리 여러개.
                    for (String keyValue : query.split("&")){
                        map.put(keyValue.split("=" , 1)[0] , keyValue.split("=" , 1)[1]);
                    }
                } else {
                    //1개만 있을 경우.
                    map.put(query.split("=" , 1)[0] , query.split("=" , 1)[1]);
                }
            } else{
                //?가 없으면 쿼리스트링도 없다는 의미이므로 null 반환.
                map = null;
            }
        } else{
            map = null;
        }
        return map;
    }
}
