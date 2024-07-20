package com.ReadAndREST.services;

import com.ReadAndREST.dto.*;
import com.ReadAndREST.models.*;
import com.ReadAndREST.repositories.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.*;
import java.util.*;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Service class for handling book-related operations.
 * This service provides methods for searching books, managing book ownership, and loading book data from a CSV file.
 * It interacts with the {@link BookRepository} and {@link UserBookMapRepository} to perform various CRUD operations.
 */
@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserBookMapRepository userBookMapRepository;

    private static final String FLASK_API_URL = "http://localhost:5000/recommend";
    private Gson gson = new Gson();

    /**
     * Searches for books based on a query string.
     *
     * This method searches for books whose titles contain the given query string, ignoring case.
     *
     * @param query the query string to search for in book titles
     * @return a list of {@link Book} objects that match the search query
     */
    public List<Book> searchBooks(String query) {
        return bookRepository.findByTitleContainingIgnoreCase(query);
    }

    /**
     * Searches for books by their title.
     *
     * This method searches for books whose titles contain the given title string, ignoring case.
     *
     * @param title the title string to search for in book titles
     * @return a list of {@link Book} objects that match the search title
     */
    public List<Book> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    /**
     * Finds a book by its ID.
     *
     * This method retrieves a {@link Book} from the repository using its ID. If no book is found with the given ID,
     * {@code null} is returned.
     *
     * @param id the ID of the book to retrieve
     * @return the {@link Book} object with the specified ID, or {@code null} if not found
     */
    public Book findById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    /**
     * Initializes the service by loading books from a CSV file.
     *
     * This method is called automatically after the bean is created. It loads book data from a CSV file located
     * in the classpath and saves it to the repository.
     */
    @PostConstruct
    public void init() {
        loadBooksFromCSV();
    }

    /**
     * Adds ownership of a book to a user.
     *
     * This method creates a new {@link UserBookMap} entry associating the given book with the given user and
     * saves it to the {@link UserBookMapRepository}.
     *
     * @param book the {@link Book} to be added to the user's collection
     * @param user the {@link User} who will own the book
     */
    public void addOwner(Book book, User user) {
        UserBookMap userBookMap = new UserBookMap();
        userBookMap.setUser(user);
        userBookMap.setBook(book);
        userBookMapRepository.save(userBookMap);
    }

    /**
     * Loads book data from a CSV file and saves it to the repository.
     *
     * This method reads book data from a CSV file located in the classpath, creates new {@link Book} instances
     * for each record, and saves them to the {@link BookRepository}. It skips records for books that already
     * exist in the repository.
     *
     * @throws IOException if an error occurs while reading the CSV file
     */
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
                genresString = genresString.replaceAll("\\[", "").replaceAll("\\]", "");
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
