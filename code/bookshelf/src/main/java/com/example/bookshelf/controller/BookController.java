package com.example.bookshelf.controller;

import com.example.bookshelf.entity.Book;
import com.example.bookshelf.repository.BookRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/book")
public class BookController {

    @Autowired
    private BookRepository books;

    // add new book
    @PostMapping
    public Book add(@RequestBody Book book) {
        return books.save(book);
    }

    // get all books (optional filter: by author's id)
    @GetMapping("/all")
    public Iterable<Book> getAll(@RequestParam Optional<Long> authorID) {

        if (authorID.isPresent()) {
            return books.findByAuthorID(authorID.get());
        } else {
            return books.findAll();
        }
    }

    // get book by ID
    @GetMapping("/{id}")
    public Optional<Book> get(@PathVariable("id") long id) {
        return books.findById(id);
    }

    // update existing book
    @PutMapping("/{id}")
    public Book update(@PathVariable("id") long id, @RequestBody Book updated) {

        // not necessary; but let us have 404 status when trying
        // to update non-exiting item (to differ from the POST case)
        if (!books.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "no book #" + id + " found");
        }

        // do not allow to change id (to avoid duplicates)
        if (updated.getId() != id) { updated.setId(id); }

        return books.save(updated);
    }

    private long parseOrThrow(String s) {

        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "invalid long: " + s);
        }
    }

    // some custom updates: use request parameters:
    // addAuthor, rmAuthor, title, publisher, publyear
    @PutMapping("/{id}/edit")
    public Book updateCustom(@PathVariable("id") long id,
            @RequestParam Map<String, String> params) {

        if (!books.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "no book #" + id + " found");
        }

        if (params.containsKey("addAuthor")) {
            long aid = parseOrThrow(params.get("addAuthor"));
            books.addAuthor(id, aid);
        }

        if (params.containsKey("rmAuthor")) {
            long aid = parseOrThrow(params.get("rmAuthor"));
            books.removeAuthor(id, aid);
        }

        Book book = books.findById(id).get();

        boolean modified = false;

        if (params.containsKey("title")) {
            book.setTitle(params.get("title"));
            modified = true;
        }

        if (params.containsKey("publisher")) {
            book.setPublisher(params.get("publisher"));
            modified = true;
        }

        if (params.containsKey("publyear")) {
            book.setPublyear(Integer.parseInt(params.get("publyear")));
            modified = true;
        }

        return modified ? books.save(book) : book;
    }

    // delete book
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) {
        books.deleteById(id);
    }
}
