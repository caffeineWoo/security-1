package com.example.security1.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String MainPage(Model model){

        System.out.println(model);
        return "Main";
    }
}
