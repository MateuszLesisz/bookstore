package com.book_shop.bookstore.catalog.db;

import com.book_shop.bookstore.catalog.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookJpaRepository extends JpaRepository<Book, Long> {
}
