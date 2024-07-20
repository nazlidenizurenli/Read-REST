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

/**
 * Controller class responsible for managing book-related operations.
 * Handles requests related to book retrieval, search, addition to the user's collection,
 * and generating book recommendations.
 */
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

    /**
     * Retrieves all books from the database.
     *
     * @return a list of all books
     */
    @GetMapping
    @RequestMapping("/all")
    public List<Book> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books;
    }

    /**
     * Searches for books based on a query string.
     *
     * @param query the search query string
     * @return a list of books matching the query
     */
    @GetMapping("/search")
    @ResponseBody
    public List<Book> searchBooks(@RequestParam("query") String query) {
        return bookService.searchBooks(query);
    }

    /**
     * Adds a book to the logged-in user's collection with a specified rating.
     * Generates recommendations if the user's collection size is 5 or more.
     *
     * @param bookId the ID of the book to add
     * @param rating the rating for the book
     * @param session the HTTP session to manage user state
     * @return a message indicating the result of the operation
     */
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

        // Check if the user's collection size is 5 or more
        List<UserBookMap> userBooks = userBookMapService.findByUser(loggedInUser);
        if (userBooks.size() >= 5) {
            try {
                // Generate recommendations and store them in session
                List<UserBookDto> recommendations = userBookMapService.checkAndSendRecommendations(userBooks, session);
                session.setAttribute("recommendations", recommendations);
            } catch (Exception e) {
                e.printStackTrace();
                return "Error in generating recommendations.";
            }
        } 
        return "Book added successfully to My Books!";
    }

    /**
     * Retrieves book recommendations for the logged-in user from the session.
     * If recommendations are not available, generates new recommendations.
     *
     * @param session the HTTP session to manage user state
     * @return a ResponseEntity containing the list of recommendations or an error status
     */
    @GetMapping("/recommendations")
    @ResponseBody
    public ResponseEntity<List<UserBookDto>> getRecommendations(HttpSession session) {        
        @SuppressWarnings("unchecked")
        List<UserBookDto> recommendations = (List<UserBookDto>) session.getAttribute("recommendations");

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            if (recommendations == null) {
                recommendations = generateAndStoreRecommendations(loggedInUser, session);
                
                if (recommendations == null) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
                }
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList());
        }
        
        return ResponseEntity.ok(recommendations);
    }

    /**
     * Generates and stores book recommendations for the logged-in user in the session.
     * Only generates recommendations if the user has 5 or more books.
     *
     * @param loggedInUser the currently logged-in user
     * @param session      the HTTP session to manage user state
     * @return a list of recommendations or an empty list if no recommendations could be generated
     */
    public List<UserBookDto> generateAndStoreRecommendations(User loggedInUser, HttpSession session) {
        List<UserBookMap> userBooks = userBookMapService.findByUser(loggedInUser);
        if (userBooks.size() >= 5) {
            try {
                // Generate recommendations
                List<UserBookDto> recommendations = userBookMapService.checkAndSendRecommendations(userBooks, session);
                // Store recommendations in session
                session.setAttribute("recommendations", recommendations);
                return recommendations;
            } catch (Exception e) {
                e.printStackTrace();
                return Collections.emptyList();  // Return an empty list in case of an error
            }
        }
        return Collections.emptyList();  // Return an empty list if userBooks.size() < 5
    }

    /**
     * Retrieves the books in the logged-in user's collection.
     *
     * @param session the HTTP session to manage user state
     * @return a ResponseEntity containing the list of books in the user's collection or an error status
     */
    @GetMapping("/getMyBooks")
    public ResponseEntity<List<UserBookDto>> getMyBooks(HttpSession session) {
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
        List<UserBookDto> userbookDtos = userBookMaps.stream()
                                            .map(userBookMap -> new UserBookDto(
                                                userBookMap.getBook().getId(),
                                                userBookMap.getBook().getTitle(),
                                                userBookMap.getBook().getAuthor(),
                                                new HashSet<>(userBookMap.getBook().getGenres()),
                                                userBookMap.getRating()
                                            ))
                                            .collect(Collectors.toList());

        // Return the list of books
        return ResponseEntity.ok(userbookDtos);
    }

}
