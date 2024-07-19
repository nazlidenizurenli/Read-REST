package com.ReadAndREST.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import com.ReadAndREST.models.*;
import com.ReadAndREST.repositories.*;

@Service
public class UserBookMapService {
    @Autowired
    private UserBookMapRepository userBookMapRepository;

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
}
