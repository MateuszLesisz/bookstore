package com.book_shop.bookstore.catalog.application;

import com.book_shop.bookstore.catalog.application.port.CatalogUseCase;
import com.book_shop.bookstore.catalog.domain.Book;
import com.book_shop.bookstore.catalog.domain.CatalogRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
class CatalogService implements CatalogUseCase {
    private final CatalogRepository catalogRepository;

    @Override
    public List<Book> findByTitle(String title) {
        return catalogRepository.findAll().stream()
                .filter(book -> book.getTitle().startsWith(title))
                .collect(toList());
    }

    @Override
    public Optional<Book> findOneByTitle(String title) {
        return catalogRepository.findAll().stream()
                .filter(book -> book.getTitle().startsWith(title))
                .findFirst();
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return catalogRepository.findAll().stream()
                .filter(book -> book.getAuthor().startsWith(author))
                .collect(toList());
    }

    @Override
    public Optional<Book> findOneByTitleAndAuthor(String title, String author) {
        return catalogRepository.findAll().stream()
                .filter(book -> book.getTitle().startsWith(title))
                .filter(book -> book.getAuthor().startsWith(author))
                .findFirst();
    }

    @Override
    public List<Book> findAll() {
        return catalogRepository.findAll();
    }

    @Override
    public void addBook(CreateBookCommand bookCommand) {
        Book book = new Book(bookCommand.getTitle(), bookCommand.getAuthor(), bookCommand.getYear(), bookCommand.getPrice());
        catalogRepository.save(book);
    }

    @Override
    public void removeById(Long id) {
        catalogRepository.removeById(id);

    }

    @Override
    public UpdateBookResponse updateBook(UpdateBookCommand updateBookCommand) {
        return catalogRepository.findById(updateBookCommand.getId())
                .map(book -> {
                    Book updatedBook = updateBookCommand.updateFields(book);
                    catalogRepository.save(updatedBook);
                    return UpdateBookResponse.SUCCESS;
                })
                .orElseGet(() -> new UpdateBookResponse(false, Collections.singletonList("Book not found with id: " + updateBookCommand.getId())));
    }

    @Override
    public Optional<Book> findById(Long id) {
        return catalogRepository.findById(id);
    }
}