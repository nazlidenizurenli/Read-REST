package com.ReadAndREST.services;

import com.ReadAndREST.models.Book;
import com.ReadAndREST.models.User;
import com.ReadAndREST.repositories.BookRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> searchBooks(String query) {
        return bookRepository.findByTitleContainingIgnoreCase(query);
    }

    public List<Book> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public Book findById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    @PostConstruct
    public void init() {
        loadBooksFromCSV();
    }

    public void addOwner(Book book, User user) {
        book.getOwners().add(user);
        bookRepository.save(book);
    }

@Transactional
public void loadBooksFromCSV() {
    try {
        ClassPathResource resource = new ClassPathResource("books.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));

        CSVParser csvParser = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .parse(reader);

        for (CSVRecord csvRecord : csvParser) {
            String title = csvRecord.get("Book");
            String author = csvRecord.get("Author");

            // Check if book already exists
            Optional<Book> existingBook = bookRepository.findByTitleAndAuthor(title, author);
            if (existingBook.isPresent()) {
                // Book already exists, skip to the next record
                continue;
            }

            // Create new book if it doesn't exist
            Book book = new Book();
            book.setTitle(title);
            book.setAuthor(author);

            String genresString = csvRecord.get("Genres");
            genresString = genresString.replaceAll("\\[", "").replaceAll("\\]", ""); // Remove brackets
            String[] genresArray = genresString.split(", ");
            Set<String> genresSet = new HashSet<>(Arrays.asList(genresArray));
            book.setGenres(genresSet);

            // Save the new book to the repository
            bookRepository.save(book);
        }

        csvParser.close();
        reader.close();

    } catch (IOException e) {
        e.printStackTrace();
    }
}
}
