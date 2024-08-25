package com.example.project_aggregator.Controller;

import com.example.project_aggregator.entity.Item;
import com.example.project_aggregator.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
public class MainController {


    @Autowired
    ItemRepository itemRepository;

    /*@Autowired
    public MainController(ItemRepositoryInterface itemRepositoryInterface){
        this.itemRepositoryInterface = itemRepositoryInterface;
    }*/

    @GetMapping("/")
    public ModelAndView templates(Model model){


        List<Item> all_items = itemRepository.selectAll();
        ModelAndView mav = new ModelAndView();
        mav.setViewName("HeaderAndSidebarTemplate");
        model.addAttribute("Test", "thymeleaf replace");
        return mav;
    }

    @GetMapping("/login")
    public ModelAndView login(Model model){

        ModelAndView mav = new ModelAndView();
        mav.setViewName("loginPage");
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
