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

    public List<BookDto> checkAndSendRecommendations(List<UserBookMap> userBooks, HttpSession session) throws Exception {
        List<UserBookDto> userBookDtos = userBooks.stream()
            .map(userBookMap -> new UserBookDto(
                userBookMap.getBook().getId(),
                userBookMap.getBook().getTitle(),
                userBookMap.getBook().getAuthor(),
                new HashSet<>(userBookMap.getBook().getGenres()),
                userBookMap.getRating() // Extract the rating
            ))
        .collect(Collectors.toList());
    
        List<Book> allBooks = bookRepository.findAll();
    
        if (userBooks.size() >= 5) {
            // Send request to Flask API
            String recommendationsJson = sendRecommendationsRequest(userBookDtos, allBooks);
    
            // Convert the recommendations to correct format
            return parseRecommendations(recommendationsJson);
        } else {
            return Collections.emptyList(); // Return an empty list if not enough books
        }
    }

    private String sendRecommendationsRequest(List<UserBookDto> userBookDtos, List<Book> allBooks) throws Exception {
        // Convert lists to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonUserBooks = objectMapper.writeValueAsString(userBookDtos);
        String jsonAllBooks = objectMapper.writeValueAsString(allBooks);
    
        // Prepare the payload
        String jsonPayload = String.format("{\"userBooks\": %s, \"allBooks\": %s}", jsonUserBooks, jsonAllBooks);
    
        // Send the POST request to the Flask API
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:5000/recommend"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();
    
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    
        return response.body(); // Return the raw JSON response
    }

    private List<BookDto> parseRecommendations(String jsonResponse) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonResponse, new TypeReference<List<BookDto>>() {});
    }
    
}
