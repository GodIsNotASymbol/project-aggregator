package com.example.project_aggregator.Controller;

import com.example.project_aggregator.Dto.MainPageItemDto;
import com.example.project_aggregator.Dto.PageNumberDto;
import com.example.project_aggregator.entity.Item;
import com.example.project_aggregator.entity.Photo;
import com.example.project_aggregator.repository.ItemRepository;
import com.example.project_aggregator.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MainController {


    @Autowired
    ItemRepository itemRepository;

    private final PhotoService photoService;

    @Autowired
    public MainController(PhotoService photoService){
        this.photoService = photoService;
    }

    private List<PageNumberDto> makePageNumberDtoList(int numberOfPages){
        List<PageNumberDto> res = new ArrayList<>();
        for(int i=1;i<=numberOfPages;i++){
            PageNumberDto pageDto = new PageNumberDto();
            pageDto.number = i;
            pageDto.redirectUrl = "mainPage?page="+i;
            res.add(pageDto);
        }
        return res;
    }

    private List<MainPageItemDto> makeMainPageItemDtosFromItems(List<Item> all_items){
        List<MainPageItemDto> res = new ArrayList<>();
        for(int i=0;i<all_items.size();i++){
            Item item = all_items.get(i);
            MainPageItemDto dto = new MainPageItemDto();
            dto.setTitle(item.getTitle());
            String base64image = this.photoService.retrievePhotoBase64ForItem(item);
            dto.setBase64Image(base64image);
            res.add(dto);
        }
        return res;
    }

    @GetMapping("/mainPage")
    public ModelAndView mainPage(@RequestParam Integer page, Model model){
        // page is just the page number
        int pageIndex = page-1;
        int pageSize = 3;
        Pageable pageWithThreeElements = PageRequest.of(pageIndex,pageSize);
        Page<Item> all_items = itemRepository.findAll(pageWithThreeElements);

        // Calculate the number of pages to make the bootstrap page selector
        int allItemsSize = itemRepository.findAll().size();
        int numberOfPages = (int) Math.floor(allItemsSize/pageSize);
        if (allItemsSize % pageSize != 0){
            numberOfPages += 1;
        }
        List<PageNumberDto> pageNumbers = makePageNumberDtoList(numberOfPages);

        // Make mainPageItemDtos for showing Title and photo on the main page
        List<MainPageItemDto> all_item_dtos = makeMainPageItemDtosFromItems(all_items.getContent());

        ModelAndView mav = new ModelAndView();
        mav.setViewName("mainPage");
        model.addAttribute("Items", all_item_dtos);
        model.addAttribute("PageNumbers", pageNumbers);
        return mav;
    }


    @GetMapping("/")
    public RedirectView redirect(){
        String url = "/mainPage?page=1";
        return new RedirectView(url);
    }

    @GetMapping("/login")
    public ModelAndView login(Model model){

        ModelAndView mav = new ModelAndView();
        mav.setViewName("loginPage");
        model.addAttribute("Test", "thymeleaf replace");
        return mav;
    }

    @GetMapping("/viewItem")
    public ModelAndView headertemplate(Model model, @RequestParam Integer item){

        ModelAndView mav = new ModelAndView();
        mav.setViewName("viewItem");
        return mav;
    }
}
