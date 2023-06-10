package com.harmyFounder.SpringBootProject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {

    @GetMapping("/")
    public String greeting(Model model, @RequestParam(value = "name", required = false) String name) {
        if (name != null && !name.isEmpty()) {
            model.addAttribute("name", name);
        } else {
            model.addAttribute("name", "World");
        }
        return "greeting";

    }
}
