package com.ReadAndREST.services;

import com.ReadAndREST.repositories.UserRepository;
import com.ReadAndREST.models.User;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    // @Autowired
    // private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void registerUser(String username, String password) {
        // String hashedPassword = passwordEncoder.encode(password);
        // User newUser = new User(username, hashedPassword);
        // userRepository.save(newUser);
    }

    public void updatePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            // String hashedPassword = passwordEncoder.encode(newPassword);
            // user.setPassword(hashedPassword);
            // userRepository.save(user);
        }
    }
}
