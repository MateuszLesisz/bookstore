package com.book_shop.bookstore.uploads.application;

import com.book_shop.bookstore.uploads.application.port.UploadUseCase;
import com.book_shop.bookstore.uploads.domain.Upload;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UploadService implements UploadUseCase {
    private final Map<String, Upload> storage = new ConcurrentHashMap<>();
    @Override
    public Upload save(SaveUploadCommand command) {
        String id = RandomStringUtils.randomAlphanumeric(8).toLowerCase();
        Upload upload = new Upload(id, command.getFile(), command.getContentType(), command.getFileName(), LocalDateTime.now());
        storage.put(upload.getId(), upload);
        System.out.println("Upload saved: " + upload.getFileName() + " with id: " + id);
        return upload;
    }

    @Override
    public Optional<Upload> getBtId(String id) {
        return Optional.ofNullable(storage.get(id));
    }
}