package com.book_shop.bookstore.catalog.domain;

import com.book_shop.bookstore.author.domain.Author;
import com.book_shop.bookstore.author.domain.RestAuthor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Getter
@NoArgsConstructor
public class RestBook {

    private Long id;
    private String title;
    private Integer year;
    private BigDecimal price;
    private String coverUrl;
    private Long available;
    private Set<RestAuthor> authors;

    public RestBook(Book book, HttpServletRequest request) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.year = book.getYear();
        this.price = book.getPrice();
        this.coverUrl = getCoverUrl(book, request);
        this.available = book.getAvailable();
        this.authors = toRestAuthor(book.getAuthors());

    }

    private Set<RestAuthor> toRestAuthor(Set<Author> authors) {
        return authors.stream()
                .map(author -> new RestAuthor(author.getName()))
                .collect(toSet());
    }

    private String getCoverUrl(Book book, HttpServletRequest request) {
        return Optional.ofNullable(book.getCoverId())
                .map(coverId1 -> ServletUriComponentsBuilder
                        .fromContextPath(request)
                        .path("/uploads/{id}/file")
                        .build(coverId1)
                        .toASCIIString())
                .orElse(null);
    }
}