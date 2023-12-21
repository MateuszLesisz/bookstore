package com.book_shop.bookstore.author.service;

import com.book_shop.bookstore.author.repository.AuthorRepository;
import com.book_shop.bookstore.author.domain.Author;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthorService {
    private final AuthorRepository repository;

    public List<Author> findAll() {
        return repository.findAll();
    }
}