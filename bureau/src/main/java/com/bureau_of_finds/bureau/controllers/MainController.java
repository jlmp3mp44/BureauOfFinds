package com.bureau_of_finds.bureau.controllers;

//import com.bureau_of_finds.bureau.models.RegistrationForm;
//import com.bureau_of_finds.bureau.repo.UserRepository;
//import org.springframework.security.crypto.password.PasswordEncoder;
import com.bureau_of_finds.bureau.models.Find;
import com.bureau_of_finds.bureau.repo.FindRepository;
import com.bureau_of_finds.bureau.services.FindService;
import com.bureau_of_finds.bureau.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class MainController {
    private FindService findService;
    private FindRepository findRepository;

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        String username = getCurrentUser();
        model.addAttribute("username", username);
        model.addAttribute("title", "Main page");

        return "home";
    }
    @GetMapping("/about_us")
    public String about_us(HttpSession session, Model model){
        String username = getCurrentUser();
        model.addAttribute("username", username);
        return "about_us";

    }


    private String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }

}