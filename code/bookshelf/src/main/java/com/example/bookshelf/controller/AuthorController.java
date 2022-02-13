package com.example.bookshelf.controller;

import com.example.bookshelf.entity.Author;
import com.example.bookshelf.entity.Book;
import com.example.bookshelf.repository.AuthorRepository;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;


@RestController
@RequestMapping(path = "/author")
public class AuthorController {

    @Autowired
    private AuthorRepository authors;

    // add new author
    @PostMapping
    public Author add(@RequestBody Author author) {

        for (Book b: author.getBooks()) {
            b.addAuthor(author);
        }

        return authors.save(author);
    }

    // get all authors (optional filter: by author's surname)
    @GetMapping("/all")
    public Iterable<Author> getAll(@RequestParam Optional<String> surname) {

        if (surname.isPresent()) {
            return authors.findBySurname(surname.get());
        } else {
            return authors.findAll();
        }
    }

    // get author by ID
    @GetMapping("/{id}")
    public Optional<Author> get(@PathVariable("id") long id) {
        return authors.findById(id);
    }

    // update existing author
    @PutMapping("/{id}")
    public Author update(
            @PathVariable("id") long id, @RequestBody Author updated) {

        // not necessary; but let us have 404 status when trying
        // to update non-exiting author (to differ from the POST case)
        if (!authors.findById(id).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "no author #" + id);
        }

        // do not allow to change id (to avoid duplicates)
        if (updated.getId() != id) { updated.setId(id); }

        return authors.save(updated);
    }

    // delete author
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) {
        authors.deleteById(id);
    }
}
