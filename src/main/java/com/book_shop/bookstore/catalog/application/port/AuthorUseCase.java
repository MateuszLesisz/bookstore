package com.book_shop.bookstore.catalog.application.port;

import com.book_shop.bookstore.catalog.domain.Author;

import java.util.List;

public interface AuthorUseCase {
    List<Author> findAll();
}