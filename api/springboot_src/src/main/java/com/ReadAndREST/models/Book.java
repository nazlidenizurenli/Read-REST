package com.ReadAndREST.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a book entity in the system.
 * This class holds information about the book, including its title, author, and genres.
 */
@Entity
@Table(
    name = "books",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"title", "author"})
    }
)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", unique = true)
    private String title;

    @Column(name = "author")
    private String author;

    @ElementCollection
    @CollectionTable(name = "book_genres", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "genre")
    private Set<String> genres = new HashSet<>();

    /**
     * Default constructor.
     * Creates an empty instance of {@link Book}.
     */
    public Book() {}

    /**
     * Parameterized constructor.
     * Creates an instance of {@link Book} with the specified title, author, and genres.
     *
     * @param title  the title of the book
     * @param author the author of the book
     * @param genres the set of genres associated with the book
     */
    public Book(String title, String author, Set<String> genres) {
        this.title = title;
        this.author = author;
        this.genres = genres;
    }

    /**
     * Gets the unique identifier of the {@link Book}.
     *
     * @return the unique identifier of the {@link Book}
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the {@link Book}.
     *
     * @param id the unique identifier to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the title of the {@link Book}.
     *
     * @return the title of the {@link Book}
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the {@link Book}.
     *
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the author of the {@link Book}.
     *
     * @return the author of the {@link Book}
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author of the {@link Book}.
     *
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Gets the set of genres associated with the {@link Book}.
     *
     * @return the set of genres associated with the {@link Book}
     */
    public Set<String> getGenres() {
        return genres;
    }

    /**
     * Sets the set of genres associated with the {@link Book}.
     *
     * @param genres the set of genres to set
     */
    public void setGenres(Set<String> genres) {
        this.genres = genres;
    }
}
