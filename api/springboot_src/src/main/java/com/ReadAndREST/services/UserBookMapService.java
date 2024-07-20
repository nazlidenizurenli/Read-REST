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

    /**
     * Retrieves a list of {@link UserBookMap} entities associated with the specified user.
     *
     * This method queries the {@link UserBookMapRepository} to find all book mappings for the given user.
     *
     * @param user the {@link User} for whom to find the book mappings
     * @return a list of {@link UserBookMap} objects associated with the specified user
     */
    public List<UserBookMap> findByUser(User user) {
        return userBookMapRepository.findByUser(user);
    }

    /**
     * Checks if a specific book is present in the user's book collection.
     *
     * This method queries the {@link UserBookMapRepository} to determine if the specified book
     * is associated with the given user. It returns {@code true} if the book is found, otherwise {@code false}.
     *
     * @param user the {@link User} whose book collection is to be checked
     * @param book the {@link Book} to check for presence in the user's collection
     * @return {@code true} if the book is present in the user's collection, otherwise {@code false}
     */
    public boolean isBookInMyBooks(User user, Book book) {
        return userBookMapRepository.findByUserAndBook(user, book).isPresent();
    }

    /**
     * Saves a {@link UserBookMap} entity to the repository.
     *
     * This method creates a new {@link UserBookMap} instance, sets the user, book, and rating,
     * and then saves the instance to the {@link UserBookMapRepository}.
     *
     * @param user the {@link User} associated with the book mapping
     * @param book the {@link Book} to be associated with the user
     * @param rating the rating given to the book by the user
     */
    public void saveUserBookMap(User user, Book book, Integer rating) {
        UserBookMap userBookMap = new UserBookMap();
        userBookMap.setUser(user);
        userBookMap.setBook(book);
        userBookMap.setRating(rating);
        userBookMapRepository.save(userBookMap);
    }

    /**
     * Checks the user's book collection, prepares recommendations, and sends them for processing.
     *
     * This method converts the list of {@link UserBookMap} entities into {@link UserBookDto} objects,
     * retrieves all books from the repository, and if the user has 5 or more books, it sends the data
     * to a Flask API for recommendations. The method then parses and returns the recommendations.
     *
     * @param userBooks a list of {@link UserBookMap} objects representing the user's book collection
     * @param session the current {@link HttpSession} for managing session attributes
     * @return a list of {@link UserBookDto} objects containing the recommendations if the user has enough books;
     *         otherwise, an empty list
     * @throws Exception if an error occurs during the recommendation request or parsing
     */
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


    /**
     * Sends a recommendation request to a Flask API.
     * This method converts lists of user book DTOs and all books to JSON, prepares a payload,
     * and sends a POST request to a Flask API to get book recommendations. The raw JSON response
     * is returned as a string.
     *
     * @param userBookDtos a list of user book data transfer objects
     * @param allBooks     a list of all books available
     * @return the raw JSON response from the Flask API as a string
     * @throws Exception if an error occurs during JSON conversion, URI creation, or the HTTP request
     */
    private String sendRecommendationsRequest(List<UserBookDto> userBookDtos, List<Book> allBooks) throws Exception {
        // Convert Data Objects to JSON.
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
        
        // Build Client and wait for response.
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Return response from client.
        System.out.println("JSON Response is: " + response);
        return response.body();
    }

    /**
     * Parses a JSON response and converts it into a list of {@link UserBookDto} objects.
     *
     * This method uses the {@link ObjectMapper} to deserialize the JSON response into a list
     * of {@link UserBookDto} instances. It expects the JSON response to be in a format that can
     * be mapped to a list of {@link UserBookDto}.
     *
     * @param jsonResponse a JSON string representing a list of user book data transfer objects
     * @return a list of {@link UserBookDto} objects parsed from the JSON response
     * @throws IOException if an error occurs during JSON parsing or deserialization
     * @see ObjectMapper
     * @see TypeReference
     */
    private List<UserBookDto> parseRecommendations(String jsonResponse) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        // TODO: Might need to create a Response Object to fit the way Python sends responses.
        return objectMapper.readValue(jsonResponse, new TypeReference<List<UserBookDto>>() {});
    }
}
