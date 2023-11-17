package com.book_shop.bookstore.order.web;

import com.book_shop.bookstore.order.application.port.PlaceOrderUseCase;
import com.book_shop.bookstore.order.domain.Order;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {

    private final PlaceOrderUseCase placeOrderUseCase;


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Order> findAllOrders() {
        return placeOrderUseCase.findAll();
    }
}
