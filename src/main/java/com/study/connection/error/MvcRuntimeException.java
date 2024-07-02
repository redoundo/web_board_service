package com.study.connection.error;

import lombok.Getter;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;

/**
 * 에러가 발생했을 때 model 에 값을 설정해 둔 뒤, 해당 html 에서 알아서 이동하게끔 설정.
 */
@Getter
public class MvcRuntimeException  extends RuntimeException{
    String path;
    public MvcRuntimeException(ErrorCode err, String html, String to, Model model){
        Map<String, Object> map = new HashMap<>();
        map.put("status", err.getStatus());
        map.put("moveTo", to);
        map.put("statusMessage", err.getStatusMessage());
        map.put("errorName", err.name());
        model.addAttribute("error", map);
        this.path = html;
    }
}
