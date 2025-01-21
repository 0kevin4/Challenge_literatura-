package com.alura.Literatura.repository;

import com.alura.Literatura.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE :language MEMBER OF b.languages")
    List<Book> findByLanguages(String language);
}
