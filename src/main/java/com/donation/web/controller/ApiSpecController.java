package com.donation.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApiSpecController {

    @GetMapping("/apispec")
    public String apiSpec(){
        return "docs/index";
    }
}
