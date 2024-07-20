package com.ReadAndREST.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Represents a mapping between a {@link User} and a {@link Book}, including a rating given by the user.
 * This entity maps users to books they own or have interacted with, along with their ratings for those books.
 */
@Entity
public class UserBookMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private Integer rating;

    /**
     * Default constructor.
     * Creates an empty instance of {@link UserBookMap}.
     */
    public UserBookMap() {}

    /**
     * Parameterized constructor.
     * Creates an instance of {@link UserBookMap} with the specified user, book, and rating.
     *
     * @param user  the {@link User} associated with the book
     * @param book  the {@link Book} associated with the user
     * @param rating the rating given by the user for the book
     */
    public UserBookMap(User user, Book book, Integer rating) {
        this.user = user;
        this.book = book;
        this.rating = rating;
    }

    /**
     * Gets the unique identifier of the {@link UserBookMap}.
     *
     * @return the unique identifier of the {@link UserBookMap}
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the {@link UserBookMap}.
     *
     * @param id the unique identifier to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the {@link User} associated with this {@link UserBookMap}.
     *
     * @return the {@link User} associated with this {@link UserBookMap}
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the {@link User} associated with this {@link UserBookMap}.
     *
     * @param user the {@link User} to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets the {@link Book} associated with this {@link UserBookMap}.
     *
     * @return the {@link Book} associated with this {@link UserBookMap}
     */
    public Book getBook() {
        return book;
    }

    /**
     * Sets the {@link Book} associated with this {@link UserBookMap}.
     *
     * @param book the {@link Book} to set
     */
    public void setBook(Book book) {
        this.book = book;
    }

    /**
     * Gets the rating given by the {@link User} for the {@link Book}.
     *
     * @return the rating given by the {@link User} for the {@link Book}
     */
    public Integer getRating() {
        return rating;
    }

    /**
     * Sets the rating given by the {@link User} for the {@link Book}.
     *
     * @param rating the rating to set
     */
    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
