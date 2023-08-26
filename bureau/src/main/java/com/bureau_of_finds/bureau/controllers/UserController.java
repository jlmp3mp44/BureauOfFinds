package com.bureau_of_finds.bureau.controllers;

import com.bureau_of_finds.bureau.models.User;
import com.bureau_of_finds.bureau.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String login(Model model) {

        String username = getCurrentUser();
        model.addAttribute("username", username);return "login";
    }

    @GetMapping("/registration")
    public String registration(Model model) {

        String username = getCurrentUser();
        model.addAttribute("username", username);return "registration";
    }


    @PostMapping("/registration")
    public String createUser(User user, Model model) {
        if (!userService.createUser(user)) {
            model.addAttribute("errorMessage", "User with this name " + user.getUsername() + " already exist");
            return "registration";
        }
        return "redirect:/login";
    }


    @GetMapping("/user/{id}")
    public String userInfo(@PathVariable("id") User user, Model model) {
        model.addAttribute("user", user);
        return "user-info";
    }
    private String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }
}

