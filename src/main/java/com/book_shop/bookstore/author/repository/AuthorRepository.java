package com.book_shop.bookstore.author.repository;

import com.book_shop.bookstore.author.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query(" SELECT DISTINCT b FROM Author b JOIN FETCH b.books")
    List<Author> findAll();
}
