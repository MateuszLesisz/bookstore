package com.book_shop.bookstore.uploads.application.port;

import com.book_shop.bookstore.uploads.domain.Upload;
import lombok.AllArgsConstructor;
import lombok.Value;

public interface UploadUseCase {

    Upload save(SaveUploadCommand command);

    @Value
    @AllArgsConstructor
    class SaveUploadCommand {
        String fileName;
        byte[] file;
        String contentType;
    }
}