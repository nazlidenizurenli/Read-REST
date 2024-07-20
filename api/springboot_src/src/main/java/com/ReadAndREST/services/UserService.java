package com.ReadAndREST.services;

import com.ReadAndREST.models.Book;
import com.ReadAndREST.models.User;
import com.ReadAndREST.models.UserBookMap;
import com.ReadAndREST.repositories.UserRepository;
import com.ReadAndREST.repositories.UserBookMapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing user-related operations.
 * This service provides methods for finding users by username, saving user data,
 * adding books to a user's collection, and retrieving users with their associated books.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserBookMapRepository userBookMapRepository;

    /**
     * Finds a user by their username.
     *
     * @param username the username of the user to be found
     * @return the {@link User} associated with the given username, or {@code null} if no user is found
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Saves the given user to the repository.
     *
     * @param user the {@link User} object to be saved
     */
    public void save(User user) {
        userRepository.save(user);
    }

    /**
     * Adds a book to a user's collection.
     * This method creates a new {@link UserBookMap} instance associating the given user with
     * the specified book, and saves this mapping to the {@link UserBookMapRepository}.
     *
     * @param user the {@link User} to whom the book is being added
     * @param book the {@link Book} to be added to the user's collection
     */
    @Transactional
    public void addBookToUser(User user, Book book) {
        UserBookMap userBookMap = new UserBookMap();
        userBookMap.setUser(user);
        userBookMap.setBook(book);
        userBookMapRepository.save(userBookMap);
    }

    /**
     * Retrieves a user along with their associated books.
     * This method finds a user by their ID and optionally loads their associated books.
     *
     * @param userId the ID of the user to be retrieved
     * @return the {@link User} with associated books if found, otherwise {@code null}
     */
    @Transactional(readOnly = true)
    public User getUserWithBooks(Long userId) {
        return userRepository.findById(userId)
                             .map(user -> {
                                 // Optionally load books here if needed
                                 return user;
                             })
                             .orElse(null);
    }
}
