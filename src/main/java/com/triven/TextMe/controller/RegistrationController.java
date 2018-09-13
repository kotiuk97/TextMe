package com.triven.TextMe.controller;


import com.triven.TextMe.domain.Role;
import com.triven.TextMe.domain.User;
import com.triven.TextMe.repos.UserRepo;
import com.triven.TextMe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
            User user,
            Map<String, Object> model){
        if (!userService.addUser(user)){
            model.put("message", "user exists!");
            return "registration";
        }
        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(
            @PathVariable String code,
            Model model){
        boolean isActivated = userService.activateUser(code);

        if (isActivated){
            model.addAttribute("message", "activated successfull");
        }else{
            model.addAttribute("message", "activation code is not found");
        }

        return "login";

    }

}
