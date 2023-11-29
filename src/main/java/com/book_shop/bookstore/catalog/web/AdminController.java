package com.book_shop.bookstore.catalog.web;

import com.book_shop.bookstore.catalog.application.port.CatalogUseCase;
import com.book_shop.bookstore.catalog.db.AuthorJpaRepository;
import com.book_shop.bookstore.catalog.domain.Author;
import com.book_shop.bookstore.catalog.domain.Book;
import com.book_shop.bookstore.order.application.port.ManipulateOrderUseCase;
import com.book_shop.bookstore.order.application.port.ManipulateOrderUseCase.OrderItemCommand;
import com.book_shop.bookstore.order.application.port.QueryOrderUseCase;
import com.book_shop.bookstore.order.domain.Recipient;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Set;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private final CatalogUseCase catalogUseCase;
    private final ManipulateOrderUseCase placeOrder;
    private final QueryOrderUseCase queryOrder;
    private final AuthorJpaRepository authorRepository;

    @PostMapping("/data")
    @Transactional
    public void initialize() {
        initData();
        placeOrder();

    }

    private void placeOrder() {
        Book effectiveJava = catalogUseCase.findOneByTitle("Effective Java").orElseThrow(() -> new IllegalArgumentException("Cannot find a book"));
        Book puzzlers = catalogUseCase.findOneByTitle("Java Puzzlers").orElseThrow(() -> new IllegalArgumentException("Cannot find a book"));

        Recipient recipient = Recipient.builder()
                .name("Jan Kowalski")
                .phone("123-456-789")
                .street("Armii Krajowej 31")
                .city("Kraków")
                .zipCode("30-150")
                .email("jan@example.org")
                .build();

        ManipulateOrderUseCase.PlaceOrderCommand command = ManipulateOrderUseCase.PlaceOrderCommand
                .builder()
                .recipient(recipient)
                .orderItem(new OrderItemCommand(effectiveJava.getId(), 12))
                .orderItem(new OrderItemCommand(puzzlers.getId(), 12))
                .build();

        ManipulateOrderUseCase.PlaceOrderResponse response = placeOrder.placeOrder(command);
        String result = response.handle(
                orderId -> "Created ORDER with id: " + orderId,
                error -> "Failed to created order: " + error
        );
        System.out.println(result);

        // list all orders
        queryOrder.findAll()
                .forEach(order -> System.out.println("GOT ORDER WITH TOTAL PRICE: " + order.totalPrice() + " DETAILS: " + order));
    }

    public void initData() {
        Author joshua = new Author("Joshua", "Bloch");
        Author neal = new Author("Neal", "Gafter");
        authorRepository.save(joshua);
        authorRepository.save(neal);
        CatalogUseCase.CreateBookCommand effectiveJava = new CatalogUseCase.CreateBookCommand("Effective Java", Set.of(joshua.getId()), 2005, BigDecimal.valueOf(79.99), 100L);
        CatalogUseCase.CreateBookCommand javaPuzzlers = new CatalogUseCase.CreateBookCommand("Java Puzzlers", Set.of(joshua.getId(), neal.getId()), 2018, BigDecimal.valueOf(99.99), 100L);

        catalogUseCase.addBook(effectiveJava);
        catalogUseCase.addBook(javaPuzzlers);
    }

}