package com.study.connection.controller;

import com.study.connection.dto.CategoryDto;
import com.study.connection.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class GetCategoryController {

    @Autowired
    private CategoryService service;

    @RequestMapping(value = {"/write" , "/write.html"} , method = RequestMethod.GET)
    public String getCategories(Model model){
        try{
            List<CategoryDto> categories = this.service.allCategories();
            model.addAttribute("categories" , categories);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return "write";
    }
}
