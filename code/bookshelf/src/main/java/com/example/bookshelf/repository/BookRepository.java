package com.example.bookshelf.repository;

import com.example.bookshelf.entity.Book;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(value = "SELECT * FROM book WHERE id in "
        + "(SELECT DISTINCT book_id FROM author_book WHERE author_id = :authorid)",
        nativeQuery = true)
    List<Book> findByAuthorID(Long authorid);

    // use these direct add/remove requests only for custom PUT /{bookid}/edit

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO author_book VALUES (:bookid, :authorid)",
            nativeQuery = true)
    void addAuthor(Long bookid, Long authorid);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM author_book WHERE author_id = :authorid AND book_id = :bookid",
            nativeQuery = true)
    void removeAuthor(Long bookid, Long authorid);
}
