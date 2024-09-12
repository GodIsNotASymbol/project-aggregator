package com.example.project_aggregator.Controller;

import com.example.project_aggregator.Dto.MainPageItemDto;
import com.example.project_aggregator.Dto.PageNumberDto;
import com.example.project_aggregator.Entity.Item;
import com.example.project_aggregator.Repository.ItemRepository;
import com.example.project_aggregator.Repository.PhotoRepository;
import com.example.project_aggregator.Service.PhotoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
public class MainController {


    @Autowired
    ItemRepository itemRepository;

    @Autowired
    PhotoRepository photoRepository;

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
            dto.setRedirectView("/viewItem?item="+item.getId());
            dto.setBase64Image(base64image);
            res.add(dto);
        }
        return res;
    }

    private List<MainPageItemDto> makeCreateAndEditItemDtosFromItems(List<Item> all_items){
        List<MainPageItemDto> res = new ArrayList<>();
        for(int i=0;i<all_items.size();i++){
            Item item = all_items.get(i);
            MainPageItemDto dto = new MainPageItemDto();
            dto.setTitle(item.getTitle());
            String base64image = this.photoService.retrievePhotoBase64ForItem(item);
            dto.setRedirectView("/editItem?item="+item.getId());
            dto.setBase64Image(base64image);
            res.add(dto);
        }
        return res;
    }

    // -------------CONTROLLERS--------------

    @GetMapping("/")
    public RedirectView redirect(){
        String url = "/mainPage?page=1";
        return new RedirectView(url);
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

    @GetMapping("/createAndEditPage")
    public ModelAndView createAndEditPage(HttpServletRequest request,  Model model,
                                          @RequestParam(value = "page", required = false) Integer page){
        // page is just the page number
        if(page == null){page = 1;}
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
        List<MainPageItemDto> all_item_dtos = makeCreateAndEditItemDtosFromItems(all_items.getContent());

        ModelAndView mav = new ModelAndView();
        mav.setViewName("createAndEditPage");
        model.addAttribute("Items", all_item_dtos);
        model.addAttribute("PageNumbers", pageNumbers);
        model.addAttribute("createItemUrl", "/createItem");


        return mav;
    }

    @GetMapping("/viewItem")
    public ModelAndView viewItem(HttpServletRequest request, Model model, @RequestParam Integer item){

        String referrer = request.getHeader("Referer");

        ModelAndView mav = new ModelAndView();
        mav.setViewName("viewItem");
        List<Integer> idlist = Arrays.asList(item);
        Item itemFound = this.itemRepository.findAllById(idlist).get(0);
        String base64Image = this.photoService.retrievePhotoBase64ForItem(itemFound);
        model.addAttribute("title", itemFound.getTitle());
        model.addAttribute("base64Image", base64Image);
        model.addAttribute("description", itemFound.getDescription());
        model.addAttribute("backButtonSrc", referrer);

        return mav;
    }

    /*@GetMapping("/loginPage")
    public ModelAndView loginPage(HttpServletRequest request, Model model){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("loginPage");
        return mav;
    }*/

    @GetMapping("/createItem")
    public ModelAndView createItemGet(HttpServletRequest request, Model model){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("createItem");
        String referrer = request.getHeader("Referer");
        model.addAttribute("backButtonSrc", referrer);
        return mav;
    }

    @PostMapping("/createItem")
    public ResponseEntity<String> createItemPost(HttpServletRequest request, Model model
            , @RequestParam("title") String title
            , @RequestParam("description") String description
            , @RequestParam("image") MultipartFile image){
        if (!image.isEmpty()) {
            Item item = new Item();
            item.setTitle(title);
            item.setDescription(description);
            item.setCreated_date(new Date());
            item.setLast_edit_date(new Date());
            itemRepository.save(item);

            String success = this.photoService.saveImage(image, item);
        } else {
            return new ResponseEntity<>("Please upload an image", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Item created success", HttpStatus.OK);
    }
}
