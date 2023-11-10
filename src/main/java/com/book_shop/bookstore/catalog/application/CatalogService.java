package com.book_shop.bookstore.catalog.application;

import com.book_shop.bookstore.catalog.application.port.CatalogUseCase;
import com.book_shop.bookstore.catalog.domain.Book;
import com.book_shop.bookstore.catalog.domain.CatalogRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
class CatalogService implements CatalogUseCase {
    private final CatalogRepository catalogRepository;

    public CatalogService(@Qualifier("schoolCatalogRepository") CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    @Override
    public List<Book> findByTitle(String title) {
        return catalogRepository.findAll().stream()
                .filter(book -> book.getTitle().startsWith(title))
                .collect(toList());
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return catalogRepository.findAll().stream()
                .filter(book -> book.getAuthor().startsWith(author))
                .collect(toList());
    }

    @Override
    public List<Book> findAll() {
        return Collections.emptyList();
    }

    @Override
    public Optional<Book> findOneByTitleAndAuthor(String title, String author) {
        return Optional.empty();
    }

    @Override
    public void addBook() {

    }

    @Override
    public void removeById(Long id) {

    }

    @Override
    public void updateBook() {

    }


}
