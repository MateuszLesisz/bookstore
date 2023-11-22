package com.book_shop.bookstore.catalog.application;

import com.book_shop.bookstore.catalog.application.port.CatalogUseCase;
import com.book_shop.bookstore.catalog.db.BookJpaRepository;
import com.book_shop.bookstore.catalog.domain.Book;
import com.book_shop.bookstore.uploads.application.port.UploadUseCase;
import com.book_shop.bookstore.uploads.application.port.UploadUseCase.SaveUploadCommand;
import com.book_shop.bookstore.uploads.domain.Upload;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
class CatalogService implements CatalogUseCase {
    private final BookJpaRepository repository;
    private final UploadUseCase upload;

    @Override
    public List<Book> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Book> findById(Long id) {
        return repository.findById(id);
    }


    @Override
    public List<Book> findByTitle(String title) {
        return repository.findAll()
                .stream()
                .filter(book -> book.getTitle().toLowerCase().startsWith(title.toLowerCase()))
                .collect(toList());
    }

    @Override
    public Optional<Book> findOneByTitle(String title) {
        return repository.findAll()
                .stream()
                .filter(book -> book.getTitle().startsWith(title))
                .findFirst();
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return repository.findAll()
                .stream()
                .filter(book -> book.getAuthor().toLowerCase().startsWith(author.toLowerCase()))
                .collect(toList());
    }

    @Override
    public Optional<Book> findOneByTitleAndAuthor(String title, String author) {
        return repository.findAll()
                .stream()
                .filter(book -> book.getTitle().startsWith(title))
                .filter(book -> book.getAuthor().startsWith(author))
                .findFirst();
    }

    @Override
    public List<Book> findByTitleAndAuthor(String title, String author) {
        return repository.findAll()
                .stream()
                .filter(book -> book.getAuthor().toLowerCase().startsWith(author.toLowerCase()))
                .filter(book -> book.getTitle().toLowerCase().startsWith(title.toLowerCase()))
                .collect(toList());
    }

    @Override
    public Book addBook(CreateBookCommand bookCommand) {
        return repository.save(bookCommand.toBook());
    }

    @Override
    public UpdateBookResponse updateBook(UpdateBookCommand updateBookCommand) {
        return repository.findById(updateBookCommand.getId())
                .map(book -> {
                    Book updatedBook = updateBookCommand.updateFields(book);
                    repository.save(updatedBook);
                    return UpdateBookResponse.SUCCESS;
                })
                .orElseGet(() -> new UpdateBookResponse(false, Collections.singletonList("Book not found with id: " + updateBookCommand.getId())));
    }

    @Override
    public void removeById(Long id) {
        repository.deleteById(id);

    }

    @Override
    public void updateBookCover(UpdateBookCoverCommand command) {
        repository.findById(command.getId())
                .ifPresent(book -> {
                    Upload savedUpload = upload.save(new SaveUploadCommand(command.getFileName(), command.getFile(), command.getContentType()));
                    book.setCoverId(savedUpload.getId());
                    repository.save(book);

        });
    }

    @Override
    public void removeBookCover(Long id) {
        repository.findById(id)
                .ifPresent(book -> {
                    if(book.getCoverId() != null) {
                        upload.removeById(book.getCoverId());
                        book.setCoverId(null);
                        repository.save(book);
                    }
                });
    }
}