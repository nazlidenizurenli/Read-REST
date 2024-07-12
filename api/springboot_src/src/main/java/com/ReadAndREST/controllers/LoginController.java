package com.ReadAndREST.controllers;

import com.ReadAndREST.models.User;
import com.ReadAndREST.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UserService userService; // Autowire UserService

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Model model) {
        User user = userService.findByUsername(username);
        
        if (user != null && user.getPassword().equals(password)) {
            return "redirect:/homepage";
        } else {
            model.addAttribute("error", "Wrong credentials or please sign-up");
            return "login";
        }
    }
    @GetMapping("/homepage")
    public String showHomePage() {
        return "homepage";
    }
}
