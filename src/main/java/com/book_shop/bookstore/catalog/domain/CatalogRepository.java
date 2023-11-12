package com.book_shop.bookstore.catalog.domain;


import java.util.List;
import java.util.Optional;


public interface CatalogRepository {
    List<Book> findAll();
    Optional<Book> findOneByTitle(String title);

    void save(Book book);

    Optional<Book> findById(Long id);

    void removeById(Long id);
}
