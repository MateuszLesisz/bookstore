package com.book_shop.bookstore.uploads.application;

import com.book_shop.bookstore.uploads.application.port.UploadUseCase;
import com.book_shop.bookstore.uploads.db.UploadJpaRepository;
import com.book_shop.bookstore.uploads.domain.Upload;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UploadService implements UploadUseCase {

    private final UploadJpaRepository uploadRepository;
    @Override
    public Upload save(SaveUploadCommand command) {
        String id = RandomStringUtils.randomAlphanumeric(8).toLowerCase();
        Upload upload = new Upload(command.getFileName(), command.getFile(), command.getContentType());
        uploadRepository.save(upload);
        System.out.println("Upload saved: " + upload.getFileName() + " with id: " + id);
        return upload;
    }

    @Override
    public Optional<Upload> getById(Long id) {
        return uploadRepository.findById(id);
    }

    @Override
    public void removeById(Long id) {
        uploadRepository.deleteById(id);
    }
}