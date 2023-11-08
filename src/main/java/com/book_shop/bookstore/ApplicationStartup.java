package com.book_shop.bookstore;

import com.book_shop.bookstore.catalog.application.CatalogController;
import com.book_shop.bookstore.catalog.domain.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationStartup implements CommandLineRunner {

    private final CatalogController catalogController;
    private final String title;
    private final Long limit;
    private final String author;

    public ApplicationStartup(
            CatalogController catalogController,
            @Value("${book.catalog.query}") String title,
            @Value("${book.catalog.limit}") Long limit,
            @Value("${book.catalog.author}") String author
    ) {
        this.catalogController = catalogController;
        this.title = title;
        this.limit = limit;
        this.author = author;
    }


    @Override
    public void run(String... args) {
        List<Book> books = catalogController.findByTitle(title);
        System.out.println("Find By Title");
        books.stream().limit(limit).forEach(System.out::println);
        System.out.println("Find By Author");
        List<Book> booksByAuthor = catalogController.findByAuthor(author);
        booksByAuthor.stream().limit(limit).forEach(System.out::println);
    }
}
