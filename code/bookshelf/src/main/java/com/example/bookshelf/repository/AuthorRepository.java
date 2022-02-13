package com.example.bookshelf.repository;

import com.example.bookshelf.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    List<Author> findBySurname(String surname);
}