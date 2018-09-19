package com.triven.TextMe.controller;

import com.triven.TextMe.domain.Message;
import com.triven.TextMe.domain.User;
import com.triven.TextMe.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Controller
public class MainController {

    @Autowired
    private MessageRepo messageRepo;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String greeting(
            Map<String, Object> model
    ){
        return "greeting";
    }

    @GetMapping("/main")
    public String main(
            @RequestParam(required = false, defaultValue = "") String filter,
            Model model){
        Iterable<Message> messages;
        if (filter != null && !filter.isEmpty()){
            messages = messageRepo.findByTag(filter);
        }else{
            messages = messageRepo.findAll();
        }
        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);
        return "main";
    }

    @PostMapping("/main")
    public String submit(
            @AuthenticationPrincipal User user,
            @Valid Message message,
            BindingResult bindingResult,            //errors after validation, always before Model
            @RequestParam(name = "file") MultipartFile file,
            Model model) throws IOException {
            message.setAuthor(user);

            if (bindingResult.hasErrors()){
                Map<String, String> errorMap = ControllerUtils.getErrors(bindingResult);
                model.mergeAttributes(errorMap);
                model.addAttribute("message", message);
            }else {
                saveFile(message, file);
                messageRepo.save(message);
                model.addAttribute("message", null);
            }

        Iterable<Message> messages = messageRepo.findAll();
        model.addAttribute("messages", messages);

        return "main";

    }

    private void saveFile(@Valid Message message, @RequestParam(name = "file") MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));

            message.setFilename(resultFilename);
        }
    }

    @GetMapping("/user-messages/{user}")
    public String userMessages(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user,
            @RequestParam(required = false) Message message,
            Model model){
        Set<Message> messages = user.getMessages();
        model.addAttribute("subscriptionsCount", user.getSubscriptions().size());
        model.addAttribute("subscribersCount", user.getSubscribers().size());
        model.addAttribute("isSubscriber", user.getSubscribers().contains(currentUser));
        model.addAttribute("userChannel", user);
        model.addAttribute("messages", messages);
        model.addAttribute("message", message);
        model.addAttribute("isCurrentUser", currentUser.equals(user));

        return "userMessages";
    }

    @PostMapping("/user-messages/{user}")
    public String updateMessage(@AuthenticationPrincipal User curentUser,
                                @PathVariable Long user,
                                @RequestParam(name = "id") Message message,
                                @RequestParam(name = "text") String text,
                                @RequestParam(name = "tag") String tag,
                                @RequestParam(name = "file") MultipartFile file) throws IOException {
        if (message.getAuthor().equals(curentUser)){
            if (!StringUtils.isEmpty(text)){
                message.setText(text);
            }
            if (!StringUtils.isEmpty(tag)){
                message.setTag(tag);
            }
            saveFile(message, file);
            messageRepo.save(message);
        }

        return "redirect:/user-messages/" + user;

    }


}
