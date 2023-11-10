package com.book_shop.bookstore;


import com.book_shop.bookstore.catalog.application.port.CatalogUseCase;
import com.book_shop.bookstore.catalog.domain.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationStartup implements CommandLineRunner {

    private final CatalogUseCase catalogUseCase;
    private final String title;
    private final Long limit;
    private final String author;

    public ApplicationStartup(
            CatalogUseCase catalogUseCase,
            @Value("${book.catalog.query}") String title,
            @Value("${book.catalog.limit}") Long limit,
            @Value("${book.catalog.author}") String author
    ) {
        this.catalogUseCase = catalogUseCase;
        this.title = title;
        this.limit = limit;
        this.author = author;
    }


    @Override
    public void run(String... args) {
        List<Book> books = catalogUseCase.findByTitle(title);
        System.out.println("Find By Title");
        books.stream().limit(limit).forEach(System.out::println);
        System.out.println("Find By Author");
        List<Book> booksByAuthor = catalogUseCase.findByAuthor(author);
        booksByAuthor.stream().limit(limit).forEach(System.out::println);
    }
}
