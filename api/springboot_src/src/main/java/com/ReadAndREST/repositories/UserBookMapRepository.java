package com.ReadAndREST.repositories;

import com.ReadAndREST.models.Book;
import com.ReadAndREST.models.User;
import com.ReadAndREST.models.UserBookMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link UserBookMap} entities.
 * This interface extends {@link JpaRepository} and provides CRUD operations for {@link UserBookMap} entities.
 * It also includes custom query methods specific to {@link UserBookMap} entities.
 */
@Repository
public interface UserBookMapRepository extends JpaRepository<UserBookMap, Long> {

    /**
     * Finds all {@link UserBookMap} entities associated with the specified {@link User}.
     *
     * This method retrieves a list of {@link UserBookMap} entities from the repository based on the provided user.
     *
     * @param user the {@link User} for whom to find the book mappings
     * @return a list of {@link UserBookMap} objects associated with the specified user
     */
    List<UserBookMap> findByUser(User user);

    /**
     * Finds a {@link UserBookMap} entity by the specified {@link User} and {@link Book}.
     *
     * This method retrieves an optional {@link UserBookMap} entity from the repository based on the provided user and book.
     * If no mapping is found, {@code Optional.empty()} is returned.
     *
     * @param user the {@link User} associated with the book mapping
     * @param book the {@link Book} associated with the user
     * @return an {@link Optional} containing the {@link UserBookMap} if found, otherwise {@code Optional.empty()}
     */
    Optional<UserBookMap> findByUserAndBook(User user, Book book);
}
