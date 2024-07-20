package com.ReadAndREST.controllers;

import com.ReadAndREST.dto.BookDto;
import com.ReadAndREST.models.User;
import com.ReadAndREST.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

/**
 * Controller class that handles user login, signup, and homepage requests.
 * This controller manages user authentication, session management, and redirects users to the appropriate views based on their authentication status.
 */
@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookController bookController;

    /**
     * Displays the login page.
     *
     * @return the name of the login view
     */
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    /**
     * Handles user login requests.
     * Validates the user's credentials and, if correct, initializes the user's session and redirects to the homepage.
     * If credentials are incorrect, it returns to the login page with an error message.
     *
     * @param username the username of the user attempting to log in
     * @param password the password of the user attempting to log in
     * @param model    the model to pass attributes to the view
     * @param session  the HTTP session to manage user state
     * @return the name of the view to render, or a redirect URL
     */
    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Model model, HttpSession session) {

        // Get user information for the entered username.
        User user = userService.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            // Clear existing session attributes and initialize for new user
            session.setAttribute("recommendations", new ArrayList<BookDto>());
            session.setAttribute("loggedInUser", user);
            bookController.generateAndStoreRecommendations(user, session);
            return "redirect:/homepage";
        } else if (user != null && !user.getPassword().equals(password)){
            model.addAttribute("error", "Wrong credentials: Check username and password.");
            return "login";
        } else {
            model.addAttribute("error", "Wrong credentials or please sign-up");
            return "login";
        }
    }

    /**
     * Handles user signup requests.
     * Checks if the username already exists; if not, creates a new user and logs them in immediately.
     * If the username already exists, returns to the login page with an error message.
     *
     * @param username the username of the user signing up
     * @param password the password of the user signing up
     * @param model    the model to pass attributes to the view
     * @param session  the HTTP session to manage user state
     * @return the name of the view to render, or a redirect URL
     */
    @PostMapping("/signup")
    public String signup(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Model model, HttpSession session) {

        // Check if user already exists
        User existingUser = userService.findByUsername(username);

        if (existingUser != null) {
            model.addAttribute("error", "Account already exists. Please login or change username and password.");
            return "login";
        } else {
            // Create new user
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            // Save the new user to the database
            userService.save(newUser);
            
            // Log in the user immediately after sign up
            session.setAttribute("loggedInUser", newUser); // Store the newly created user in session

            session.setAttribute("recommendations", new ArrayList<BookDto>());
            bookController.generateAndStoreRecommendations(newUser, session);

            return "redirect:/homepage";
        }
    }

    /**
     * Displays the homepage for the logged-in user.
     * If the user is logged in, it passes user data to the homepage view. 
     * If the user is not logged in, it redirects to the login page.
     *
     * @param model   the model to pass attributes to the view
     * @param session the HTTP session to retrieve user state
     * @return the name of the view to render, or a redirect URL
     */
    @GetMapping("/homepage")
    public String showHomePage(Model model, HttpSession session) { // Add HttpSession parameter
        // Retrieve logged-in user from session
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            // Pass user data to the homepage view
            model.addAttribute("user", loggedInUser);
            return "homepage";
        } else {
            // Handle case where user is not logged in (optional)
            return "redirect:/login";
        }
    }
}
