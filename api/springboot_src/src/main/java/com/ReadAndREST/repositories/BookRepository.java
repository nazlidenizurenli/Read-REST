package com.ReadAndREST.repositories;

import com.ReadAndREST.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Book} entities.
 * This interface extends {@link JpaRepository} and provides CRUD operations for {@link Book} entities.
 * It also includes custom query methods specific to {@link Book} entities.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Finds a list of {@link Book} entities with titles containing the specified search string.
     *
     * This method performs a case-insensitive search for books whose titles contain the given string.
     *
     * @param title the string to search for within book titles
     * @return a list of {@link Book} objects with titles containing the specified search string
     */
    List<Book> findByTitleContainingIgnoreCase(String title);

    /**
     * Finds a {@link Book} entity with the specified title.
     *
     * This method retrieves a single {@link Book} from the repository based on the exact title.
     * If no book is found with the given title, {@code null} is returned.
     *
     * @param title the title of the book to find
     * @return a {@link Book} object with the specified title, or {@code null} if not found
     */
    Book findByTitle(String title);

    /**
     * Finds a {@link Book} entity with the specified title and author.
     *
     * This method retrieves an optional {@link Book} from the repository based on the exact title and author.
     * If no book is found with the given title and author, {@code Optional.empty()} is returned.
     *
     * @param title  the title of the book to find
     * @param author the author of the book to find
     * @return an {@link Optional} containing the {@link Book} if found, otherwise {@code Optional.empty()}
     */
    Optional<Book> findByTitleAndAuthor(String title, String author);
}
