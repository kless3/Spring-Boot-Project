package com.harmyFounder.SpringBootProject.controllers;


import com.harmyFounder.SpringBootProject.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    private final UserRepo userRepo;

    public UserController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/")
    public String getUsers(Model model){
        model.addAttribute("users", userRepo.findAll());
        return "all";
    }


}
