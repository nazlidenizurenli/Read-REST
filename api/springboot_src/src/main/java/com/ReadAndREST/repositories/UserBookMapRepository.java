package com.ReadAndREST.repositories;

import com.ReadAndREST.models.Book;
import com.ReadAndREST.models.User;
import com.ReadAndREST.models.UserBookMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface UserBookMapRepository extends JpaRepository<UserBookMap, Long> {
    List<UserBookMap> findByUser(User user);

    Optional<UserBookMap> findByUserAndBook(User user, Book book);
}
