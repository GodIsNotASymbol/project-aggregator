package com.example.project_aggregator.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @GetMapping("/")
    String greetings(){
        return "HelloWorld";
    }
}
