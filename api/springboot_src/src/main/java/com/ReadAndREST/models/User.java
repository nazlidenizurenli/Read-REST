package com.ReadAndREST.models;

import javax.persistence.*;

/**
 * Represents a user in the system.
 * This entity stores user information, including the username and password.
 */
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    /**
     * Default constructor.
     * Creates an empty instance of {@link User}.
     */
    public User() {}

    /**
     * Parameterized constructor.
     * Creates an instance of {@link User} with the specified username and password.
     *
     * @param username the username of the user
     * @param password the password of the user
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Gets the unique identifier of the {@link User}.
     *
     * @return the unique identifier of the {@link User}
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the {@link User}.
     *
     * @param id the unique identifier to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the username of the {@link User}.
     *
     * @return the username of the {@link User}
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the {@link User}.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password of the {@link User}.
     *
     * @return the password of the {@link User}
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the {@link User}.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
