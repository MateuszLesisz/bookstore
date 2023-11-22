package com.book_shop.bookstore;


import com.book_shop.bookstore.catalog.application.port.CatalogUseCase;
import com.book_shop.bookstore.catalog.application.port.CatalogUseCase.UpdateBookCommand;
import com.book_shop.bookstore.catalog.domain.Book;
import com.book_shop.bookstore.order.application.port.PlaceOrderUseCase;
import com.book_shop.bookstore.order.application.port.PlaceOrderUseCase.PlaceOrderCommand;
import com.book_shop.bookstore.order.application.port.PlaceOrderUseCase.PlaceOrderResponse;
import com.book_shop.bookstore.order.application.port.QueryOrderUseCase;
import com.book_shop.bookstore.order.domain.OrderItem;
import com.book_shop.bookstore.order.domain.Recipient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class ApplicationStartup implements CommandLineRunner {

    private final CatalogUseCase catalogUseCase;
    private final PlaceOrderUseCase placeOrder;
    private final QueryOrderUseCase queryOrder;
    private final String title;
    private final Long limit;
    private final String author;

    public ApplicationStartup(
            CatalogUseCase catalogUseCase,
            PlaceOrderUseCase placeOrder,
            QueryOrderUseCase queryOrder,
            @Value("${book.catalog.query}") String title,
            @Value("${book.catalog.limit}") Long limit,
            @Value("${book.catalog.author}") String author
    ) {
        this.catalogUseCase = catalogUseCase;
        this.title = title;
        this.limit = limit;
        this.author = author;
        this.placeOrder = placeOrder;
        this.queryOrder = queryOrder;
    }


    @Override
    public void run(String... args) {
        initData();
        searchCatalog();
        placeOrder();
    }

    private void placeOrder() {
        Book harryPotter = catalogUseCase.findOneByTitle("Harry").orElseThrow(() -> new IllegalArgumentException("Cannot find a book"));

        Recipient recipient = Recipient.builder()
                .name("Jan Kowalski")
                .phone("123-456-789")
                .street("Armii Krajowej 31")
                .city("Kraków")
                .zipCode("30-150")
                .email("jan@example.org")
                .build();

        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient)
                .orderItem(new OrderItem(harryPotter.getId(), 12))
                .build();
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
                    UpdateBookCommand command = UpdateBookCommand.builder()
                            .id(book.getId())
                            .title("Harry Tadeusz czyli ostatni zjazd na Litwie")
                            .build();
                    CatalogUseCase.UpdateBookResponse response = catalogUseCase.updateBook(command);
                    System.out.println("Updating book result: " + response.isSuccess());
                });
    }

    public void initData() {
        catalogUseCase.addBook(new CatalogUseCase.CreateBookCommand("Harry", "Adam Mickiewicz", 1834, new BigDecimal("12.99")));
        catalogUseCase.addBook(new CatalogUseCase.CreateBookCommand("Hobbit", "Henryk Sienkiewicz", 1834, new BigDecimal("12.99")));
        catalogUseCase.addBook(new CatalogUseCase.CreateBookCommand("Wiedźmin", "Władysław Reymont", 1834, new BigDecimal("12.99")));
    }

    private void findByTitle() {
        List<Book> books = catalogUseCase.findByTitle(title);
        System.out.println("Find By Title");
        books.stream().limit(limit).forEach(System.out::println);

    }
}
