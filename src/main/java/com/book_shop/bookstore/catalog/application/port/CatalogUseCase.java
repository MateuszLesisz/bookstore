package com.book_shop.bookstore.catalog.application.port;

import com.book_shop.bookstore.catalog.domain.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.emptyList;

public interface CatalogUseCase {
    List<Book> findByTitle(String title);
    Optional<Book> findOneByTitle(String title);
    List<Book> findByAuthor(String author);
    List<Book> findAll();
    List<Book> findByTitleAndAuthor(String title, String author);
    Book addBook(CreateBookCommand createBookCommand);
    void removeById(Long id);
    UpdateBookResponse updateBook(UpdateBookCommand updateBookCommand);
    Optional<Book> findById(Long id);
    void updateBookCover(UpdateBookCoverCommand command);
    void removeBookCover(Long id);

    @Value
    class CreateBookCommand {

        String title;
        Set<Long> authors;
        Integer year;
        BigDecimal price;
    }

    @Value
    @Builder
    @AllArgsConstructor
    class UpdateBookCommand {

        Long id;
        String title;
        Set<Long> authors;
        Integer year;
        BigDecimal price;
    }

    @Value
    class UpdateBookResponse {
        public static final UpdateBookResponse SUCCESS = new UpdateBookResponse(true, emptyList());

        boolean success;
        List<String> errors;
    }

    @Value
    class UpdateBookCoverCommand {
        Long id;
        byte[] file;
        String contentType;
        String fileName;
    }
}