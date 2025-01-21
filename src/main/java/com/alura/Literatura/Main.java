package com.alura.Literatura;

import com.alura.Literatura.models.*;
import com.alura.Literatura.repository.AuthorRepository;
import com.alura.Literatura.repository.BookRepository;
import com.alura.Literatura.service.APIconsumption;
import com.alura.Literatura.service.ConvertData;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private Scanner scanner = new Scanner(System.in);
    private final String url = "https://gutendex.com/books/?search=";
    private APIconsumption apIconsumption = new APIconsumption();
    private ConvertData convertData = new ConvertData();
    private BookRepository bookRepository;
    private AuthorRepository authorRepository;

    public Main(BookRepository bookRepository, AuthorRepository authorRepository) {

        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public void showMenu(){

        var exit = -1;
        while (exit != 0)
        {
            var Choice = "----------------------\n" +
                    "Elija la opcion a traves de su numero\n" +
                    "1- Buscar libro por titulo\n" +
                    "2- Listar libros registrados\n" +
                    "3- Listar autores registrados\n" +
                    "4- Listar autores en un determinado año\n" +
                    "5- Listar libro por idioma\n" +
                    "0 -Salir";
            System.out.println(Choice);
            exit = scanner.nextInt();
            scanner.nextLine();

            switch (exit)
            {
                case 1:
                    bookBytitle();
                    break;
                case 2:
                    listRegisteredBooks();
                    break;
                case 3:
                    listRegisteredAuthors();
                    break;
                case 4:
                    authorsByYear();
                    break;
                case 5:
                    booksByLanguage();
                    break;
                case 0:
                    System.out.printf("\nSaliendo...\n\n");
                    break;
                default:
                    System.out.println("Opción no válida");
                    return;
            }
        }

    }

    private DataResults getDataResults(){
        System.out.println("Escriba el nombre del libro que desea buscar");
        var keyboard = scanner.nextLine();
        var json = apIconsumption.getData(url + keyboard.replace(" ", "+") );
        DataResults dataResults = convertData.getData(json, DataResults.class);
        return dataResults;
    }

    private void bookBytitle() {

       var dataResults = getDataResults();

        Optional<Book> books = dataResults.dataBooks().stream()
                .map(e -> new Book(e, e.authors().stream()
                        .map(s -> new Author(s))
                        .collect(Collectors.toList())))
                .findFirst();

        if (books.isPresent())
        {
            var bookFound = books.get();
            System.out.println(books.get());

            List<Book> book = new ArrayList<>();
            book.add(bookFound);

            List<Author> authors = book.stream()
                    .flatMap(s->s.getAuthors().stream())
                    .collect(Collectors.toList());

            bookFound.setAuthors(authors);
            bookRepository.save(books.get());
        }
        else
        {
            System.out.printf("\nLibro no encontrado\n\n");
        }
    }

    private void listRegisteredBooks(){
        List<Book> books =  bookRepository.findAll();
        if(books.isEmpty())
        {
            System.out.println("\nAutor no encontrado\n");
        }
        else
        {
            books.stream().forEach(System.out::println);
        }
    }

    private void listRegisteredAuthors(){

        List<Author> authors = authorRepository.findAllWithBook();

        if(authors.isEmpty())
        {
            System.out.println("\nAutor no encontrado\n");
        }
        else{
            Map<String, List<Author>> authorsGroupedByName = authors.stream()
                    .collect(Collectors.groupingBy(Author::getName));


            authorsGroupedByName.forEach((authorName, authorList) -> {

                Author firstAuthor = authorList.get(0);
                System.out.println("\nAutor: " + firstAuthor.getName());
                System.out.println("Fecha de nacimiento: " + firstAuthor.getBirthdate());
                System.out.println("Fecha de fallecimiento: " + firstAuthor.getDeathYear());

                String bookTitles = authorList.stream()
                        .map(author -> author.getBook().getTitle())
                        .distinct()
                        .collect(Collectors.joining(", "));
                System.out.println("Libros: " + bookTitles +"\n");
            });
        }


    }

    private void authorsByYear(){
        System.out.println("Ingrese el año:");
        int year = scanner.nextInt();

        List<Author> authors = authorRepository.findAuthorsAliveInYear(year);

        if(authors.isEmpty())
        {
            System.out.println("\nAutor no encontrado\n");
        }
        else
        {
            Map<String, Set<String>> authorsGroupedByName = authors.stream()
                    .collect(Collectors.groupingBy(
                            Author::getName,
                            Collectors.mapping(
                                    author -> author.getBook().getTitle(),
                                    Collectors.toSet()
                            )
                    ));

            authorsGroupedByName.forEach((name, books) -> {
                Author author = authors.stream()
                        .filter(a -> a.getName().equals(name))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Autor no encontrado"));

                System.out.println("\nAutor: " + name);
                System.out.println("Fecha de nacimiento: " + author.getBirthdate());
                System.out.println("Fecha de fallecimiento: " + author.getDeathYear());


                String bookTitles = authors.stream()
                        .map(a -> a.getBook().getTitle())
                        .distinct()
                        .collect(Collectors.joining(", "));
                System.out.println("Libros: " + bookTitles +"\n");
            });
        }
    }
    private void booksByLanguage(){

        var Choice = "----------------------\n" +
                "Elija la opcion a traves de su numero\n" +
                "1- es/español\n" +
                "2- en/ingles\n" +
                "3- fr/frances\n" +
                "4- pt/portuges\n"+
                "0- Salir";

        System.out.println(Choice);
        int option = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer

        List<Book> books = new ArrayList<>();

        switch (option) {
            case 1:
                books = bookRepository.findByLanguages("es");
                break;
            case 2:
                books = bookRepository.findByLanguages("en");
                break;
            case 3:
                books = bookRepository.findByLanguages("fr");
                break;
            case 4:
                books = bookRepository.findByLanguages("pt");
                break;
            case 0:
                System.out.println("Saliendo...");
                return;
            default:
                System.out.println("Opción no válida");
                return;
        }

        if (books.isEmpty()) {
            System.out.println("\nNo se encontraron libros en ese idioma.\n");
        } else {
            books.forEach(book -> System.out.println(book));
        }


    };


}