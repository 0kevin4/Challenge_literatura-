package com.alura.Literatura.models;

import jakarta.persistence.*;

@Entity
@Table(name = "Autores")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String name;
    private Integer birthdate;
    private Integer deathYear;
    @ManyToOne
    private Book book;

    public Author() {
    }

    public Author(DataAuthors dataAuthors) {
        this.name = dataAuthors.name();
        this.birthdate = dataAuthors.birthYear();
        this.deathYear = dataAuthors.deathYear();
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Integer birthdate) {
        this.birthdate = birthdate;
    }

    public Integer getDeathYear() {
        return deathYear;
    }

    public void setDeathYear(Integer deathYear) {
        this.deathYear = deathYear;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public String toString() {
        return "Author{" +
                "Id=" + Id +
                ", name='" + name + '\'' +
                ", birthdate=" + birthdate +
                ", deathYear=" + deathYear;
    }

    //    @Override
//    public String toString() {
//        return "\n---------Autor---------\n" +
//                "Titulo: " + name +
//                "\nAño de Nacimiento: " + birthdate +
//                "\nAño de fallecimiento: " + deathYear +
//                "\nLibros: " + book +
//                "\n-----------------------\n";
//    }
}