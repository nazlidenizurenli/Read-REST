package com.ReadAndREST.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import com.ReadAndREST.dto.UserBookDto;
import com.ReadAndREST.models.*;
import com.ReadAndREST.repositories.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserBookMapService {
    @Autowired
    private UserBookMapRepository userBookMapRepository;

    @Autowired
    private BookRepository bookRepository;

    public List<UserBookMap> findByUser(User user) {
        return userBookMapRepository.findByUser(user);
    }

    public boolean isBookInMyBooks(User user, Book book) {
        return userBookMapRepository.findByUserAndBook(user, book).isPresent();
    }

    public void saveUserBookMap(User user, Book book, Integer rating) {
        UserBookMap userBookMap = new UserBookMap();
        userBookMap.setUser(user);
        userBookMap.setBook(book);
        userBookMap.setRating(rating);
        userBookMapRepository.save(userBookMap);
    }

    public List<UserBookDto> checkAndSendRecommendations(List<UserBookMap> userBooks, HttpSession session) throws Exception {
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
        System.out.println("JSON Response is: " + jsonUserBooks);
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
        System.out.println("JSON Response is: " + response);
        return response.body(); // Return the raw JSON response
    }

    private List<UserBookDto> parseRecommendations(String jsonResponse) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonResponse, new TypeReference<List<UserBookDto>>() {});
    }
}
