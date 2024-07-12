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

        // Get user information for the entered username.
        User user = userService.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            return "redirect:/homepage";
        } else if (user != null && !user.getPassword().equals(password)){
            model.addAttribute("error", "Wrong credentials: Check username and password.");
            return "login";
        } else {
            model.addAttribute("error", "Wrong credentials or please sign-up");
            return "login";
        }
    }

    @PostMapping("/signup")
    public String signup(@RequestParam("username") String username,
                         @RequestParam("password") String password,
                         Model model) {

        // Check if user already exists
        User existingUser = userService.findByUsername(username);

        if (existingUser != null && existingUser.getPassword().equals(password)) {
            model.addAttribute("error", "Account already exists. Please login or change username and password.");
            return "redirect:/login";
        } else {
            // Create new user
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password); // In a real application, you should hash the password

            // Save the new user to the database
            userService.save(newUser);
            return "redirect:/homepage";
        }
    }

    @GetMapping("/homepage")
    public String showHomePage() {
        return "homepage";
    }
}
