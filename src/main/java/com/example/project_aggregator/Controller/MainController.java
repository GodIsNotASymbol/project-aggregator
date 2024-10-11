package com.example.project_aggregator.Controller;

import com.example.project_aggregator.Dto.MainPageItemDto;
import com.example.project_aggregator.Dto.PageNumberDto;
import com.example.project_aggregator.Entity.Item;
import com.example.project_aggregator.Repository.ItemRepository;
import com.example.project_aggregator.Repository.PhotoRepository;
import com.example.project_aggregator.Service.PhotoService;
import io.pebbletemplates.pebble.PebbleEngine;
import io.pebbletemplates.pebble.template.PebbleTemplate;
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

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.http.HttpResponse;
import java.util.*;

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

    private List<PageNumberDto> makePageNumberDtoListForCreateAndEditPage(int numberOfPages){
        List<PageNumberDto> res = new ArrayList<>();
        for(int i=1;i<=numberOfPages;i++){
            PageNumberDto pageDto = new PageNumberDto();
            pageDto.number = i;
            pageDto.redirectUrl = "createAndEditPage?page="+i;
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
    public String mainPage(@RequestParam Integer page, Model model) throws IOException {
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


        PebbleEngine engine = new PebbleEngine.Builder().build();
        PebbleTemplate compiledTemplate = engine.getTemplate("templates/mainPage.html");

        Map<String, Object> context = new HashMap<>();
        context.put("Items", all_item_dtos);
        context.put("PageNumbers", pageNumbers);

        Writer writer = new StringWriter();
        compiledTemplate.evaluate(writer, context);
        String output = writer.toString();
        return output;
    }

    @GetMapping("/createAndEditPage")
    public String createAndEditPage(HttpServletRequest request,  Model model,
                                          @RequestParam(value = "page", required = false) Integer page) throws IOException {
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
        List<PageNumberDto> pageNumbers = makePageNumberDtoListForCreateAndEditPage(numberOfPages);

        // Make mainPageItemDtos for showing Title and photo on the main page
        List<MainPageItemDto> all_item_dtos = makeCreateAndEditItemDtosFromItems(all_items.getContent());

        PebbleEngine engine = new PebbleEngine.Builder().build();
        PebbleTemplate compiledTemplate = engine.getTemplate("templates/createAndEditPage.html");

        Map<String, Object> context = new HashMap<>();
        context.put("Items", all_item_dtos);
        context.put("PageNumbers", pageNumbers);
        context.put("createItemUrl", "/createItem");

        Writer writer = new StringWriter();
        compiledTemplate.evaluate(writer, context);
        String output = writer.toString();
        return output;
    }

    @GetMapping("/viewItem")
    public String viewItem(HttpServletRequest request, Model model, @RequestParam Integer item) throws IOException {

        String referrer = request.getHeader("Referer");

        List<Integer> idlist = Arrays.asList(item);
        Item itemFound = this.itemRepository.findAllById(idlist).get(0);
        String base64Image = this.photoService.retrievePhotoBase64ForItem(itemFound);

        PebbleEngine engine = new PebbleEngine.Builder().build();
        PebbleTemplate compiledTemplate = engine.getTemplate("templates/viewItem.html");

        Map<String, Object> context = new HashMap<>();
        context.put("title", itemFound.getTitle());
        context.put("base64Image", base64Image);
        context.put("description", itemFound.getDescription());
        context.put("backButtonSrc", referrer);

        Writer writer = new StringWriter();
        compiledTemplate.evaluate(writer, context);
        String output = writer.toString();
        return output;
    }

    /*@GetMapping("/loginPage")
    public ModelAndView loginPage(HttpServletRequest request, Model model){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("loginPage");
        return mav;
    }*/

    @GetMapping("/createItem")
    public String createItemGet(HttpServletRequest request, Model model) throws IOException{

        PebbleEngine engine = new PebbleEngine.Builder().build();
        PebbleTemplate compiledTemplate = engine.getTemplate("templates/createItem.html");

        Map<String, Object> context = new HashMap<>();
        String referrer = request.getHeader("Referer");
        context.put("backButtonSrc", referrer);

        Writer writer = new StringWriter();
        compiledTemplate.evaluate(writer, context);
        String output = writer.toString();
        return output;
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

    @GetMapping("/editItem")
    public ModelAndView editItemGet(HttpServletRequest request, Model model, @RequestParam Integer item){

        List<Integer> idlist = Arrays.asList(item);
        Item itemFound = this.itemRepository.findAllById(idlist).get(0);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("editItem");
        String referrer = request.getHeader("Referer");
        model.addAttribute("backButtonSrc", referrer);
        model.addAttribute("itemId", itemFound.getId());
        return mav;
    }

    @PostMapping("/editItem")
    public ResponseEntity<String> editItemPost(HttpServletRequest request, Model model
            , @RequestParam("title") String title
            , @RequestParam("description") String description
            , @RequestParam("image") MultipartFile image
            , @RequestParam("item") Integer itemId){
        if (!image.isEmpty()) {
            Item item = itemRepository.findById(itemId).get();
            item.setTitle(title);
            item.setDescription(description);
            item.setLast_edit_date(new Date());
            itemRepository.save(item);

            this.photoService.deleteImageForItem(item);
            String success = this.photoService.saveImage(image, item);
        } else {
            return new ResponseEntity<>("Please upload an image", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Item edited success", HttpStatus.OK);
    }
}
