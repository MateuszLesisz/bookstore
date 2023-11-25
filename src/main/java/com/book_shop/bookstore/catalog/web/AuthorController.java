package com.book_shop.bookstore.catalog.web;

import com.book_shop.bookstore.catalog.application.port.AuthorUseCase;
import com.book_shop.bookstore.catalog.domain.Author;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorUseCase authors;

    @GetMapping
    public List<Author> findAll() {
        return authors.findAll();
    }
}