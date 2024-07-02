package com.study.connection.error;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorControllerAdvice {
    @ExceptionHandler(MvcRuntimeException.class)
    public String handleError(MvcRuntimeException err){
        return err.getPath();
    }

}
