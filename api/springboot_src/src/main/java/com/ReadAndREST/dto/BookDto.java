package com.ReadAndREST.dto;

import java.util.Set;

public class BookDto {
    private Long id;
    private String title;
    private String author;
    private Set<String> genres;

    // Constructor
    public BookDto(Long id, String title, String author, Set<String> genres) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genres = genres;
    }

    // Getters and Setters
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

    public Set<String> getGenres() {
        return genres;
    }

    public void setGenres(Set<String> genres) {
        this.genres = genres;
    }
}
