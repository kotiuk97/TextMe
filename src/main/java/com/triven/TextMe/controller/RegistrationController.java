package com.triven.TextMe.controller;


import com.triven.TextMe.domain.Role;
import com.triven.TextMe.domain.User;
import com.triven.TextMe.domain.dto.CaptchaResponseDTO;
import com.triven.TextMe.repos.UserRepo;
import com.triven.TextMe.service.UserService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {

    private static final String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";
    @Autowired
    private UserService userService;

    @Value("${recaptcha.secret}")
    private String recaptchaSecret;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
        public String addUser(
                @RequestParam("g-recaptcha-response") String captchaResponse,
                @RequestParam("password2") String passwordConfirmation,
                @Valid User user,
                BindingResult bindingResult,
                Model model){

        String URL = String.format(CAPTCHA_URL, recaptchaSecret, captchaResponse);
        CaptchaResponseDTO captchaResponseDTO = restTemplate.postForObject(URL, Collections.EMPTY_LIST, CaptchaResponseDTO.class);
        if (!captchaResponseDTO.isSuccess()){
            model.addAttribute("captchaError", "Fill captcha");
        }

        boolean isConfirmEmpty = Strings.isEmpty(passwordConfirmation);
        if (isConfirmEmpty){
            model.addAttribute("password2Error", "Password confirmation cannot be empty");
        }
        if (user.getPassword() != null && !user.getPassword().equals(passwordConfirmation)){
            model.addAttribute("password2Error", "passwords are not equal");
        }
        if (isConfirmEmpty || bindingResult.hasErrors() || !captchaResponseDTO.isSuccess()){
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);

            return "registration";
        }

        if (!userService.addUser(user)){
            model.addAttribute("usernameError", "User exists!");
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
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "activated successfull");
        }else{
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "activation code is not found");
        }

        return "login";

    }

}
