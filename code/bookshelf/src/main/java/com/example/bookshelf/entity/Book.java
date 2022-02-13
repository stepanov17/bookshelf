package com.example.bookshelf.entity;

import java.util.HashSet;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.ManyToMany;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @Column(name = "id")
    private Long id; // in fact, Integer is enough. but let it be long

    @ManyToMany
    @JoinTable(
            name="author_book",
            joinColumns = @JoinColumn(name="book_id"),
            inverseJoinColumns = @JoinColumn(name="author_id")
    )
    private Set<Author> authors = new HashSet<>();

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "publisher", nullable = false)
    private String publisher;

    @Column(name = "publyear", nullable = false)
    private Integer publyear;

    private Book() {}
    public Book(Long id) { this.id = id; }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public void addAuthor(Author author) {
        authors.add(author);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getPublyear() {
        return publyear;
    }

    public void setPublyear(int publyear) {
        this.publyear = publyear;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) { return true; }
        if (!(other instanceof Book)) { return false; }

        return Objects.equals(id, ((Book) other).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
