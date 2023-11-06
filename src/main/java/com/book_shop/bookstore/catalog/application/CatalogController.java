package com.book_shop.bookstore.catalog.application;

import com.book_shop.bookstore.catalog.domain.Book;
import com.book_shop.bookstore.catalog.domain.CatalogService;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    public List<Book> findByTitle(String title) {
        return catalogService.findByTitle(title);
    }
}
