package com.book_shop.bookstore.catalog.domain;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class CatalogService {
    private final CatalogRepository catalogRepository;

    public CatalogService(@Qualifier("schoolCatalogRepository") CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    public List<Book> findByTitle(String title) {
        return catalogRepository.findAll().stream()
                .filter(book -> book.getTitle().startsWith(title))
                .collect(toList());
    }


}
