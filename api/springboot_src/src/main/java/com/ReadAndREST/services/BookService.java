package com.ReadAndREST.services;

import com.ReadAndREST.dto.*;
import com.ReadAndREST.models.*;
import com.ReadAndREST.repositories.*;
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


@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserBookMapRepository userBookMapRepository;

    private static final String FLASK_API_URL = "http://localhost:5000/recommend";
    private Gson gson = new Gson();

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
        UserBookMap userBookMap = new UserBookMap();
        userBookMap.setUser(user);
        userBookMap.setBook(book);
        userBookMapRepository.save(userBookMap);
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

    public void checkAndSendRecommendations(List<UserBookMap> userBooks, HttpSession session) throws Exception {
        // Convert UserBookMap to UserBookDto
        List<UserBookDto> userBookDtos = userBooks.stream()
            .map(userBookMap -> new UserBookDto(
                userBookMap.getBook().getId(),
                userBookMap.getBook().getTitle(),
                userBookMap.getBook().getAuthor(),
                new HashSet<>(userBookMap.getBook().getGenres()), // Convert to Set if necessary
                userBookMap.getRating() // Extract the rating
            ))
        .collect(Collectors.toList());
        
        // Get all books
        List<Book> allBooks = bookRepository.findAll();

        // Check if the user has 5 or more books
        if (userBooks.size() >= 5) {
            // Send request to Flask API
            String recommendations = sendRecommendationsRequest(userBookDtos, allBooks);

            // TODO: Convert the recommendations to correct format
            // TODO: Return these reccomendations. We need to fix the function signature and maybe add an endpoint so html and js access the return result.
            // Handle the response (e.g., display recommendations)
            System.out.println("Recommendations: " + recommendations);
        } else {
            System.out.println("Not enough books to generate recommendations.");
        }
    }

    private String sendRecommendationsRequest(List<UserBookDto> userBooks, List<Book> allBooks) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        // Convert Java objects to JSON
        String jsonPayload = gson.toJson(new RecommendationRequest(userBooks, allBooks));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(FLASK_API_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
    
}
