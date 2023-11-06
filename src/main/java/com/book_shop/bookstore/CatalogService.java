package com.book_shop.bookstore;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toList;

@Service
public class CatalogService {
    private final Map<Long, Book> storage = new ConcurrentHashMap<>();

    public CatalogService() {
        storage.put(1L, new Book(1L, "Pan Tadeusz", "Adam Mickiewicz", 1834));
        storage.put(2L, new Book(2L, "Ogniem i mieczem", "Henryk Sienkiewicz", 1834));
        storage.put(3L, new Book(3L, "Chłopi", "Władysław Reymont", 1834));
    }

    List<Book> findByTitle(String title) {
        return storage.values().stream()
                .filter(book -> book.title.startsWith(title))
                .collect(toList());

    }


}
