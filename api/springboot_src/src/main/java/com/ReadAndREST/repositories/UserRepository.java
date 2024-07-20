package com.ReadAndREST.repositories;

import com.ReadAndREST.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link User} entities.
 * This interface extends {@link JpaRepository} and provides CRUD operations for {@link User} entities.
 * It also includes custom query methods specific to {@link User} entities.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a {@link User} by their username.
     *
     * This method retrieves a {@link User} entity from the repository based on the provided username.
     * If no user is found with the given username, {@code null} is returned.
     *
     * @param username the username of the user to find
     * @return the {@link User} entity with the specified username, or {@code null} if not found
     */
    User findByUsername(String username);
}
