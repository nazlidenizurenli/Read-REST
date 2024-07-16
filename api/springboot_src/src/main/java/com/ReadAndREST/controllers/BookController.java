package com.ReadAndREST.controllers;

import com.ReadAndREST.models.Book;
import com.ReadAndREST.repositories.BookRepository;
import com.ReadAndREST.services.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

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
        List<Book> books = bookService.searchBooks(query);
        for (Book book : books) {
            System.out.println("Book Title: " + book.getTitle());
        }
        return bookService.searchBooks(query);
    }
}
