package com.book_shop.bookstore.uploads.db;

import com.book_shop.bookstore.uploads.domain.Upload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadJpaRepository extends JpaRepository<Upload, Long> {
}