package com.book_shop.bookstore.catalog.application;

import com.book_shop.bookstore.catalog.domain.Book;
import com.book_shop.bookstore.catalog.domain.CatalogService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@AllArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;

    public List<Book> findByTitle(String title) {
        return catalogService.findByTitle(title);
    }

    public List<Book> findByAuthor(String author) {
        return catalogService.findByAuthor(author);
    }
}
