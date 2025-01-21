package com.alura.Literatura.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "Libros")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String title;
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Author> authors;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> languages;
    private Long download_count;

    public Book() {
    }

    public Book(DataBook dataBook, List<Author> dataAuthors) {
        this.title = dataBook.title();
        this.authors = dataAuthors;
        this.languages = dataBook.languages();
        this.download_count = dataBook.download_count();
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public Long getDownload_count() {
        return download_count;
    }

    public void setDownload_count(Long download_count) {
        this.download_count = download_count;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        authors.forEach(s->s.setBook(this));
        this.authors = authors;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    @Override
    public String toString() {
        String authorNames = authors.stream()
                .map(Author::getName)
                .collect(Collectors.joining(", "));

        return "\n---------LIBRO---------\n" +
                "Titulo: " + title +
                "\nAutores: " + authorNames +
                "\nIdiomas: " + languages +
                "\nNumero de descargas: " + download_count+
                "\n-----------------------\n";

    }
}