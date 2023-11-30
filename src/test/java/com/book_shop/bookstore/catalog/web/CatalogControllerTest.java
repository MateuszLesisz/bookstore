package com.book_shop.bookstore.catalog.web;

import com.book_shop.bookstore.catalog.application.port.CatalogUseCase;
import com.book_shop.bookstore.catalog.domain.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CatalogController.class})
class CatalogControllerTest {

    @MockBean
    CatalogUseCase catalogUseCase;
    @Autowired
    CatalogController catalogController;

    @Test
    public void shouldGetAllBooks() {
        //given
        Book effectiveJava = new Book("Effective Java", 2005, new BigDecimal("99.90"), 10L);
        Book concurrency = new Book("Java Concurrency", 2006, new BigDecimal("129.00"), 50L);
        when(catalogUseCase.findAll()).thenReturn(List.of(effectiveJava, concurrency));

        //when
        List<Book> all = catalogController.getAll(Optional.empty(), Optional.empty());

        //then
        assertEquals(2, all.size());
    }
}