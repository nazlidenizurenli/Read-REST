package com.ReadAndREST.dto;

import java.util.Set;

/**
 * Data Transfer Object (DTO) for representing a book and its associated details.
 * This class is used to transfer book data between different layers of the application, such as from the service layer to the presentation layer.
 */
public class BookDto {

    private Long id;
    private String title;
    private String author;
    private Set<String> genres;

    /**
     * Default constructor.
     * Creates an empty instance of {@link BookDto}.
     */
    public BookDto() {
    }

    /**
     * Parameterized constructor.
     * Creates an instance of {@link BookDto} with the specified id, title, author, and genres.
     *
     * @param id      the unique identifier of the book
     * @param title   the title of the book
     * @param author  the author of the book
     * @param genres  the set of genres associated with the book
     */
    public BookDto(Long id, String title, String author, Set<String> genres) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genres = genres;
    }

    /**
     * Gets the unique identifier of the book.
     *
     * @return the unique identifier of the book
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the book.
     *
     * @param id the unique identifier to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the title of the book.
     *
     * @return the title of the book
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the book.
     *
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the author of the book.
     *
     * @return the author of the book
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author of the book.
     *
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Gets the set of genres associated with the book.
     *
     * @return the set of genres associated with the book
     */
    public Set<String> getGenres() {
        return genres;
    }

    /**
     * Sets the set of genres associated with the book.
     *
     * @param genres the set of genres to set
     */
    public void setGenres(Set<String> genres) {
        this.genres = genres;
    }
}
