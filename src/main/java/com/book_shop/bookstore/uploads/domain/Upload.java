package com.book_shop.bookstore.uploads.domain;

import com.book_shop.bookstore.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Upload extends BaseEntity {

    private Long id;
    private byte[] file;
    private String contentType;
    private String fileName;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Upload(String fileName, byte[] file, String contentType) {
        this.fileName = fileName;
        this.file = file;
        this.contentType = contentType;
    }
}