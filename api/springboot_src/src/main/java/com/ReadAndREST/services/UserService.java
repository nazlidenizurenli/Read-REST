package com.ReadAndREST.services;

import com.ReadAndREST.models.Book;
import com.ReadAndREST.models.User;
import com.ReadAndREST.models.UserBookMap;
import com.ReadAndREST.repositories.UserRepository;
import com.ReadAndREST.repositories.UserBookMapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserBookMapRepository userBookMapRepository;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void addBookToUser(User user, Book book) {
        UserBookMap userBookMap = new UserBookMap();
        userBookMap.setUser(user);
        userBookMap.setBook(book);
        userBookMapRepository.save(userBookMap);
    }

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
