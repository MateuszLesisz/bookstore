package com.book_shop.bookstore.catalog.db;

import com.book_shop.bookstore.catalog.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorJpaRepository extends JpaRepository<Author, Long> {
}