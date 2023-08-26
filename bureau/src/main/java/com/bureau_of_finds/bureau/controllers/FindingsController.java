package com.bureau_of_finds.bureau.controllers;

import com.bureau_of_finds.bureau.models.Find;
import com.bureau_of_finds.bureau.models.User;
import com.bureau_of_finds.bureau.repo.FindRepository;
import com.bureau_of_finds.bureau.repo.UserRepository;
import com.bureau_of_finds.bureau.services.CustomUserDetailsService;
import com.bureau_of_finds.bureau.services.FindService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.bureau_of_finds.bureau.services.CustomUserDetailsService.getId;


@Controller
@RequiredArgsConstructor
public class FindingsController {
    @Autowired
    private FindRepository findRepository;
    private final FindService findService;
    private final UserRepository userRepository;

    @GetMapping("/find")
    public String find(Model model) {
        Iterable<Find> find = findRepository.findAll();
        List<Find> reversedFind = new ArrayList<>();
        find.forEach(reversedFind::add);
        Collections.reverse(reversedFind);
        model.addAttribute("findings", reversedFind);
        String username = getCurrentUser();
        model.addAttribute("username", username);
        return "findings";
    }


    @GetMapping("/add")
    public String add(Model model) {
        String username = getCurrentUser();
        model.addAttribute("username", username);
        return "add";
    }

    @PostMapping("/add")
    public String FindPostAdd(@RequestParam String title, @RequestParam String key_words,
                              @RequestParam String description, @RequestParam String telephone,
                              @RequestParam("file") MultipartFile file, Model model) throws IOException {

        Find find = new Find(title, key_words, description, telephone);
        // Obtain the user based on your criteria, e.g., the current authenticated user
        User user = userRepository.findByUsername(getCurrentUser()); // Replace getCurrentUser() with the appropriate method to get the current user
        if (user != null) {
            find.setUser(user);
            findService.saveFind(find, file);
            model.addAttribute("find", find);
            String username = getCurrentUser();
            model.addAttribute("username", username);

            return "ready_add";
        } else {
            // Handle the case when the user is not found
            model.addAttribute("message", "User not found");
            String username = getCurrentUser();
            model.addAttribute("username", username);
            return "redirect:/";
        }

    }

    private String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }
    @GetMapping("/search")
    public String searchByKey( @RequestParam String keywords, Model model) {
        String username = getCurrentUser();
        model.addAttribute("username", username);
        String key = keywords;
        if (keywords.isEmpty()) return "redirect:/";
        Iterable<Find> allSearch = findRepository.findAll();
        ArrayList<Find> res = new ArrayList<>();
        for (Find search : allSearch) {
            if (search.getKey_words().contains(keywords)) {
                res.add(search);
            }
        }
        model.addAttribute("key", key);
        model.addAttribute("searchings", res);
        return "search";
    }



    @GetMapping("/find{id}")
    public String findDetails(@PathVariable(value = "id") Long id, Model model) {
        if (!findRepository.existsById(id))
            return "redirect:/";
        Optional<Find> find = findRepository.findById(id);
        ArrayList<Find> res = new ArrayList<>();
        find.ifPresent(res::add);
        model.addAttribute("find", res);
        String username = getCurrentUser();
        model.addAttribute("username", username);
        return "find-details";
    }

    @GetMapping("/find/{id}/delete")
    public String findDelete(@PathVariable(value = "id") Long id, Model model) {
        String username = getCurrentUser();
        model.addAttribute("username", username);
        if (!findRepository.existsById(id)) {
            return "redirect:/";
        }

        Find find = findRepository.findById(id).orElseThrow();

        // Retrieve the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByUsername(authentication.getName());
        User findOwner = find.getUser();

        boolean isAuthenticatedBeforeDeletion = authentication.isAuthenticated();

        if (findOwner != null && currentUser != null && findOwner.getId().equals(currentUser.getId())) {
            // Delete the find
            findRepository.delete(find);
            model.addAttribute("message", "Delete a find");

            boolean isAuthenticatedAfterDeletion = authentication.isAuthenticated();
            System.out.println("Is authenticated before deletion: " + isAuthenticatedBeforeDeletion);
            System.out.println("Is authenticated after deletion: " + isAuthenticatedAfterDeletion);

            return "ready_delete";
        } else {
            model.addAttribute("errorMessage", "Cannot delete a find, You`re not a Owner");
            return "find-details";
        }
    }

}




