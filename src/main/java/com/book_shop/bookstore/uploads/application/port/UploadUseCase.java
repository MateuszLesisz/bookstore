package com.book_shop.bookstore.uploads.application.port;

import com.book_shop.bookstore.uploads.domain.Upload;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.Optional;

public interface UploadUseCase {

    Upload save(SaveUploadCommand command);
    Optional<Upload> getBtId(String id);
    void removeById(String id);

    @Value
    @AllArgsConstructor
    class SaveUploadCommand {
        String fileName;
        byte[] file;
        String contentType;
    }
}