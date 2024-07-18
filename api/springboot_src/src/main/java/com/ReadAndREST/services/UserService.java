package com.ReadAndREST.services;

import com.ReadAndREST.repositories.UserRepository;
import com.ReadAndREST.models.Book;
import com.ReadAndREST.models.User;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    // @Autowired
    // private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void addBookToUser(User user, Book book) {
        user = userRepository.findById(user.getId()).orElse(null);
        user.getMyBooks().add(book);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getUserWithBooks(Long userId) {
        return userRepository.findById(userId)
                             .map(user -> {
                                 user.getMyBooks().size();
                                 return user;
                             })
                             .orElse(null);
    }

}
