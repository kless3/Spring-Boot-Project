package com.harmyFounder.SpringBootProject.controllers;

import com.harmyFounder.SpringBootProject.models.User;
import com.harmyFounder.SpringBootProject.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {

    private final UserRepo userRepo;

    public RegistrationController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/register")
    public String getRegistrationPage() {
        return "registration";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name, @RequestParam String email, @RequestParam String group, @RequestParam String number, @RequestParam int course) {
        User user = new User(name, email, group, number, course);

        userRepo.save(user);

        return "redirect:/";
    }


}
