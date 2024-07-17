package com.ReadAndREST.repositories;

import com.ReadAndREST.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCase(String title);

    Book findByTitle(String title);

    Optional<Book> findByTitleAndAuthor(String title, String author);
}
