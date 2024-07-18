package com.ReadAndREST.controllers;

import com.ReadAndREST.models.Book;
import com.ReadAndREST.models.User;
import com.ReadAndREST.dto.BookDto;
import com.ReadAndREST.repositories.BookRepository;
import com.ReadAndREST.services.BookService;
import com.ReadAndREST.services.UserService;

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

    @GetMapping
    public List<Book> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books;
    }

    @Autowired
    private BookService bookService;

    @GetMapping("/search")
    @ResponseBody
    public List<Book> searchBooks(@RequestParam("query") String query) {
        return bookService.searchBooks(query);
    }

    @PostMapping("/add")
    @ResponseBody
    public String addBookToUser(@RequestParam Long bookId, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        // Debug 
        System.out.println("User information" + loggedInUser.getUsername() + loggedInUser.getPassword());
        if (loggedInUser != null) {
            Book book = bookService.findById(bookId);
            if (book != null) {
                userService.addBookToUser(loggedInUser, book);
                bookService.addOwner(book, loggedInUser);
                return "Book added successfully!";
            } else {
                return "Book not found!";
            }
        } else {
            return "User not logged in.";
        }
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

        // Retrieve the list of books for the user
        Set<Book> myBooks = user.getMyBooks();

        // Convert Book entities to DTOs
        List<BookDto> bookDtos = myBooks.stream()
                                        .map(book -> new BookDto(book.getId(), book.getTitle(), book.getAuthor(), book.getGenres()))
                                        .collect(Collectors.toList());

        // Return the list of books
        return ResponseEntity.ok(bookDtos);
    }
}
