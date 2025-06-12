package com.harmyFounder.SpringBootProject.controller;

import com.harmyFounder.SpringBootProject.model.User;
import com.harmyFounder.SpringBootProject.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/secured")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/user")
    public String getUserAccess(Principal principal){
        if(principal == null){
            return null;
        }

        return principal.getName();

    }





}
