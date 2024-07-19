package com.ReadAndREST.controllers;

import com.ReadAndREST.models.*;
import com.ReadAndREST.dto.*;
import com.ReadAndREST.repositories.*;
import com.ReadAndREST.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserBookMapService userBookMapService;

    @Autowired
    private BookService bookService;

    @GetMapping
    @RequestMapping("/all")
    public List<Book> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books;
    }

    @GetMapping("/search")
    @ResponseBody
    public List<Book> searchBooks(@RequestParam("query") String query) {
        return bookService.searchBooks(query);
    }

    @PostMapping("/add")
    @ResponseBody
    public String addBookToUser(@RequestParam Long bookId, @RequestParam Integer rating, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "User not logged in.";
        }

        // Find the book by ID
        Book bookToAdd = bookService.findById(bookId);
        if (bookToAdd == null) {
            return "Book not found.";
        }

        // Check if the book is already in the user's My Books
        if (userBookMapService.isBookInMyBooks(loggedInUser, bookToAdd)) {
            return "Book is already added to your My Books.";
        }

        // Add book to user's collection
        userBookMapService.saveUserBookMap(loggedInUser, bookToAdd, rating);
        return "Book added successfully to My Books!";
    }


    @GetMapping("/getMyBooks")
public ResponseEntity<List<BookDto>> getMyBooks(HttpSession session) {
    User user = (User) session.getAttribute("loggedInUser");
    if (user == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    user = userService.getUserWithBooks(user.getId());

    if (user == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // Get books for the user from UserBookMap
    List<UserBookMap> userBookMaps = userBookMapService.findByUser(user);

    // Convert Book entities to DTOs
    List<BookDto> bookDtos = userBookMaps.stream()
                                         .map(userBookMap -> new BookDto(
                                             userBookMap.getBook().getId(),
                                             userBookMap.getBook().getTitle(),
                                             userBookMap.getBook().getAuthor(),
                                             new HashSet<>(userBookMap.getBook().getGenres()), // Convert to Set if necessary
                                             userBookMap.getRating() // Extract the rating
                                         ))
                                         .collect(Collectors.toList());

    // Return the list of books
    return ResponseEntity.ok(bookDtos);
}


}
