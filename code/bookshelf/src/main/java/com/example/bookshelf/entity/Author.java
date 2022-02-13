package com.example.bookshelf.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.ManyToMany;

import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


@Entity
@Table(name = "author")
public class Author {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "name")
    private String name;

    @Column(name = "patronymic")
    private String patronymic;

    @JsonIgnoreProperties("authors") // to avoid recursion in json (or @JsonBackReference)
    @ManyToMany(mappedBy = "authors")
    private Set<Book> books = new HashSet<>();

    private Author() {}
    public Author(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSurame() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) { return true; }
        if (!(other instanceof Author)) { return false; }

        return Objects.equals(id, ((Author) other).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
