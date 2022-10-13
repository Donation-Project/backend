package com.donation.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApiSpecContoller {

    @GetMapping("/")
    public String apiSpec(){
        return "docs/index";
    }
}
