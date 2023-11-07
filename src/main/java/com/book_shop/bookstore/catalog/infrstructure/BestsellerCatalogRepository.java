package com.book_shop.bookstore.catalog.infrstructure;

import com.book_shop.bookstore.catalog.domain.Book;
import com.book_shop.bookstore.catalog.domain.CatalogRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
class BestsellerCatalogRepository implements CatalogRepository {
    private final Map<Long, Book> storage = new ConcurrentHashMap<>();

    public BestsellerCatalogRepository() {
        storage.put(1L, new Book(1L, "Harry Potter", "Adam Mickiewicz", 1834));
        storage.put(2L, new Book(2L, "Hobbit", "Henryk Sienkiewicz", 1834));
        storage.put(3L, new Book(3L, "Wiedźmin", "Władysław Reymont", 1834));
    }
    @Override
    public List<Book> findAll() {
        return new ArrayList<>(storage.values());
    }
}
