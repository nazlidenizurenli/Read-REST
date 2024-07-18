package com.ReadAndREST.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    @ManyToMany
    @JoinTable(
        name = "user_book",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private Set<Book> myBooks = new HashSet<>();

    // Constructors, getters, and setters
    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Book> getMyBooks() {
        return myBooks;
    }

    public void setMyBooks(Set<Book> myBooks) {
        this.myBooks = myBooks;
    }
}
