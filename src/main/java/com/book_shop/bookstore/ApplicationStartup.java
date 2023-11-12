package com.book_shop.bookstore;


import com.book_shop.bookstore.catalog.application.port.CatalogUseCase;
import com.book_shop.bookstore.catalog.domain.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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
        initData();
        searchCatalog();
        placeOrder();
    }

    private void placeOrder() {

    }

    private void searchCatalog() {
        findByTitle();
        findAndUpdate();
        findByTitle();
    }

    private void findAndUpdate() {
        System.out.println("Updating book.....");
        catalogUseCase.findOneByTitleAndAuthor("Harry Potter", "Adam")
                .ifPresent(book -> {
                    CatalogUseCase.UpdateBookCommand command = CatalogUseCase.UpdateBookCommand.builder()
                            .id(book.getId())
                            .title("Harry Tadeusz czyli ostatni zjazd na Litwie")
                            .build();
                    CatalogUseCase.UpdateBookResponse response = catalogUseCase.updateBook(command);
                    System.out.println("Updating book result: " + response.isSuccess());
                });
    }

    public void initData() {
        catalogUseCase.addBook(new CatalogUseCase.CreateBookCommand("Harry Potter", "Adam Mickiewicz", 1834, new BigDecimal("12.99")));
        catalogUseCase.addBook(new CatalogUseCase.CreateBookCommand("Hobbit", "Henryk Sienkiewicz", 1834, new BigDecimal("12.99")));
        catalogUseCase.addBook(new CatalogUseCase.CreateBookCommand("Wiedźmin", "Władysław Reymont", 1834, new BigDecimal("12.99")));
    }

    private void findByTitle() {
        List<Book> books = catalogUseCase.findByTitle(title);
        System.out.println("Find By Title");
        books.stream().limit(limit).forEach(System.out::println);

    }
}
