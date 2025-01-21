package com.alura.Literatura.repository;

import com.alura.Literatura.models.Author;
import com.alura.Literatura.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Query("SELECT a FROM Author a JOIN FETCH a.book")
    List<Author> findAllWithBook();

    @Query("SELECT a FROM Author a LEFT JOIN FETCH a.book WHERE :year >= a.birthdate AND (:year <= a.deathYear OR a.deathYear IS NULL)")
    List<Author> findAuthorsAliveInYear(@Param("year") int year);


}
