package com.book_shop.bookstore;

import com.book_shop.bookstore.catalog.application.CatalogController;
import com.book_shop.bookstore.catalog.domain.Book;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationStartup implements CommandLineRunner {

    private final CatalogController catalogController;

    public ApplicationStartup(CatalogController catalogController) {
        this.catalogController = catalogController;
    }

    @Override
    public void run(String... args) {
        List<Book> books = catalogController.findByTitle("Pan Tadeusz");
        books.forEach(System.out::println);
    }
}
