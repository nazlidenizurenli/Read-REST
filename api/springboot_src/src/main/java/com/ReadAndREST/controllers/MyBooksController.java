package com.ReadAndREST.controllers;

import com.ReadAndREST.models.Book;
import com.ReadAndREST.models.User;
import com.ReadAndREST.repositories.BookRepository;
import com.ReadAndREST.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/mybooks")
public class MyBooksController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add")
    public String addBookToMyBooks(@RequestBody Long bookId) {
        System.out.println("Geldim burdayim");
        // Retrieve the current session
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();

        // Get the user from session attribute
        User currentUser = (User) session.getAttribute("currentUser");
        System.out.println("Current User is: " + currentUser.getUsername() + currentUser.getPassword());

        // Find the book by ID
        Book bookToAdd = bookRepository.findById(bookId).orElse(null);
        System.out.println("Adding the book: " + bookToAdd.getTitle() + bookToAdd.getId());

        if (currentUser != null && bookToAdd != null) {
            // Add book to user's collection
            currentUser.getMyBooks().add(bookToAdd);
            userRepository.save(currentUser);
            return "Book added successfully to My Books!";
        } else {
            return "Failed to add book. User or book not found.";
        }
    }
}
