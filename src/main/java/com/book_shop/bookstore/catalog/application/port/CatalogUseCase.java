package com.book_shop.bookstore.catalog.application.port;

import com.book_shop.bookstore.catalog.domain.Book;
import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

public interface CatalogUseCase {
    List<Book> findByTitle(String title);

    List<Book> findByAuthor(String author);

    List<Book> findAll();

    Optional<Book> findOneByTitleAndAuthor(String title, String author);

    void addBook(CreateBookCommand createBookCommand);

    void removeById(Long id);

    UpdateBookResponse updateBook(UpdateBookCommand updateBookCommand);

    @Value
    class CreateBookCommand {
        String title;
        String author;
        Integer year;
    }

    @Value
    @Builder
    class UpdateBookCommand {
        Long id;
        String title;
        String author;
        Integer year;

        public Book updateFields(Book book) {
            if (title != null) {
                book.setTitle(title);
            }
            if (author != null) {
                book.setAuthor(author);
            }
            if (year != null) {
                book.setYear(year);
            }
            return book;
        }
    }

    @Value
    class UpdateBookResponse {
        public static final UpdateBookResponse SUCCESS = new UpdateBookResponse(true, emptyList());

        boolean success;
        List<String> errors;
    }
}