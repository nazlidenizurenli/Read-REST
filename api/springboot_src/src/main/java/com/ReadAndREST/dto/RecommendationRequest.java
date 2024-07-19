package com.ReadAndREST.dto;
import com.ReadAndREST.models.Book;

import java.util.List;

public class RecommendationRequest {
    private List<UserBookDto> userBooks;
    private List<Book> allBooks;

    // Default constructor
    public RecommendationRequest() {}

    // Parameterized constructor
    public RecommendationRequest(List<UserBookDto> userBooks, List<Book> allBooks) {
        this.userBooks = userBooks;
        this.allBooks = allBooks;
    }

    // Getters and setters
    public List<UserBookDto> getUserBooks() {
        return userBooks;
    }

    public void setUserBooks(List<UserBookDto> userBooks) {
        this.userBooks = userBooks;
    }

    public List<Book> getAllBooks() {
        return allBooks;
    }

    public void setAllBooks(List<Book> allBooks) {
        this.allBooks = allBooks;
    }
}

