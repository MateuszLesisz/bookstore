package com.book_shop.bookstore.catalog.application;

import com.book_shop.bookstore.catalog.application.port.CatalogInitializerUseCase;
import com.book_shop.bookstore.catalog.application.port.CatalogUseCase;
import com.book_shop.bookstore.catalog.application.port.CatalogUseCase.CreateBookCommand;
import com.book_shop.bookstore.catalog.application.port.CatalogUseCase.UpdateBookCoverCommand;
import com.book_shop.bookstore.catalog.db.AuthorJpaRepository;
import com.book_shop.bookstore.catalog.domain.Author;
import com.book_shop.bookstore.catalog.domain.Book;
import com.book_shop.bookstore.jpa.BaseEntity;
import com.book_shop.bookstore.order.application.port.ManipulateOrderUseCase;
import com.book_shop.bookstore.order.application.port.QueryOrderUseCase;
import com.book_shop.bookstore.order.domain.Recipient;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Slf4j
@Service
@AllArgsConstructor
public class CatalogInitializerService implements CatalogInitializerUseCase {

    private final CatalogUseCase catalogUseCase;
    private final ManipulateOrderUseCase placeOrder;
    private final QueryOrderUseCase queryOrder;
    private final AuthorJpaRepository authorRepository;
    private final RestTemplate restTemplate;

    @Override
    @Transactional
    public void initialize() {
        initData();
        placeOrder();
    }

    private void initData() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource("books.csv").getInputStream()))) {
            CsvToBean<CsvBook> build = new CsvToBeanBuilder<CsvBook>(reader)
                    .withType(CsvBook.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            build.stream().forEach(this::initBook);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to parse CSV file", e);
        }
    }

    private void initBook(CsvBook csvBook) {
        Set<Long> authors = Arrays.stream(csvBook.authors.split(","))
                .filter(StringUtils::isNotBlank)
                .map(String::trim)
                .map(this::getOrCreateAuthor)
                .map(BaseEntity::getId)
                .collect(toSet());

        CreateBookCommand command = new CreateBookCommand(csvBook.title, authors, csvBook.year, csvBook.amount, 50L);
        Book book = catalogUseCase.addBook(command);
        catalogUseCase.updateBookCover(updateBookCoverCommand(book.getId(), csvBook.thumbnail));
    }

    private Author getOrCreateAuthor(String name) {
        return authorRepository.findByNameIgnoreCase(name)
                .orElseGet(() -> authorRepository.save(new Author(name)));
    }

    private UpdateBookCoverCommand updateBookCoverCommand(Long bookId, String thumbnailUrl) {
        ResponseEntity<byte[]> response = restTemplate.exchange(thumbnailUrl, HttpMethod.GET, null, byte[].class);
        String contentType = response.getHeaders().getContentType().toString();
        return new UpdateBookCoverCommand(bookId, response.getBody(), contentType, "cover");
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
                .orderItem(new ManipulateOrderUseCase.OrderItemCommand(effectiveJava.getId(), 12))
                .orderItem(new ManipulateOrderUseCase.OrderItemCommand(puzzlers.getId(), 12))
                .build();

        ManipulateOrderUseCase.PlaceOrderResponse response = placeOrder.placeOrder(command);
        String result = response.handle(
                orderId -> "Created ORDER with id: " + orderId,
                error -> "Failed to created order: " + error
        );
        log.info(result);

        // list all orders
        queryOrder.findAll()
                .forEach(order -> log.info("GOT ORDER WITH TOTAL PRICE: " + order.totalPrice() + " DETAILS: " + order));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CsvBook {
        @CsvBindByName
        private String title;
        @CsvBindByName
        private String authors;
        @CsvBindByName
        private Integer year;
        @CsvBindByName
        private BigDecimal amount;
        @CsvBindByName
        private String thumbnail;
    }
}
