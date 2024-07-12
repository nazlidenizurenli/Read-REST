package com.ReadAndREST.models;

import javax.persistence.*;
import java.util.List;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Book")
    private String title;
    private String author;

    @ElementCollection
    private List<String> genres;

    // Default constructor required by Hibernate
    public Book() {}

    // Parameterized constructor for creating new books
    public Book(String title, String author, List<String> genres) {
        this.title = title;
        this.author = author;
        this.genres = genres;
    }

    // Getters and setters (ensure they are correctly implemented)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
}


