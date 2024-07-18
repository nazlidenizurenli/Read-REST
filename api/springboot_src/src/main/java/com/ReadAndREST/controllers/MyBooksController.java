package com.ReadAndREST.controllers;

import com.ReadAndREST.models.*;
import com.ReadAndREST.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/mybooks")
public class MyBooksController {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserBookMapService userBookMapService;

    @PostMapping("/add")
    public String addBookToMyBooks(@RequestBody Long bookId) {
        // Retrieve the current session
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();

        // Get the user from session attribute
        User currentUser = (User) session.getAttribute("loggedInUser");
        if (currentUser == null) {
            return "User not logged in.";
        }

        // Find the book by ID
        Book bookToAdd = bookService.findById(bookId);
        if (bookToAdd == null) {
            return "Book not found.";
        }

        // Check if the book is already in the user's My Books
        if (userBookMapService.isBookInMyBooks(currentUser, bookToAdd)) {
            return "Book is already added to your My Books.";
        }

        // Add book to user's collection
        userBookMapService.saveUserBookMap(currentUser, bookToAdd);
        return "Book added successfully to My Books!";
    }
}
