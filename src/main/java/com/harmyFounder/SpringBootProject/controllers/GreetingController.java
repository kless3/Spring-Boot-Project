package com.harmyFounder.SpringBootProject.controllers;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GreetingController {

    @GetMapping("/")
    public String getList(){
        return "list";
    }

}
