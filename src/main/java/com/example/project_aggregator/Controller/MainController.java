package com.example.project_aggregator.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.TemplateEngine;

@RestController
public class MainController {

    @GetMapping("/")
    public ModelAndView templates(Model model){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("HeaderAndSidebarTemplate");
        model.addAttribute("Test", "thymeleaf replace");
        return mav;
    }

    @GetMapping("/helloworld")
    public String helloworld(){
        return "HelloWorld";
    }

    @GetMapping("/headertemplate")
    public ModelAndView headertemplate(Model model){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("HeaderAndSidebarTemplate");
        model.addAttribute("Test", "thymeleaf replace");
        return mav;
    }
}
