package com.book_shop.bookstore.order.web;

import com.book_shop.bookstore.order.application.port.PlaceOrderUseCase;
import com.book_shop.bookstore.order.application.port.QueryOrderUseCase;
import com.book_shop.bookstore.order.domain.Order;
import com.book_shop.bookstore.order.domain.OrderItem;
import com.book_shop.bookstore.order.domain.OrderStatus;
import com.book_shop.bookstore.order.domain.Recipient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import static com.book_shop.bookstore.order.application.port.PlaceOrderUseCase.*;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {

    private final QueryOrderUseCase queryOrderUseCase;
    private final PlaceOrderUseCase placeOrderUseCase;


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Order> findAllOrders() {
        return queryOrderUseCase.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return queryOrderUseCase
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> addOrder(@Valid @RequestBody RestOrderCommand command) {
        return ResponseEntity.created(createOrderUri(placeOrderUseCase.addOrder(command.toOrderCommand()))).build();
    }

    private static URI createOrderUri(Order order) {
        return ServletUriComponentsBuilder.fromCurrentRequestUri().path("/" + order.getId().toString()).build().toUri();
    }

    @PutMapping("/{id}/status")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateOrder(@PathVariable Long id,
                            @RequestBody RestOrderCommand command) {
        UpdateOrderResponse response = placeOrderUseCase.updateOrder(command.toUpdateOrderCommand(id));
        if (!response.isSuccess()) {
            String message = String.join(", ", response.getErrors());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeOrder(@PathVariable Long id) {
        placeOrderUseCase.removeOrder(id);
    }

    @Data
    private static class RestOrderCommand {
        private OrderStatus status = OrderStatus.NEW;
        @NotEmpty
        private List<OrderItem> items;
        @NotNull
        private Recipient recipient;
        @NotNull
        private LocalDateTime createdAt;

        OrderCommand toOrderCommand() {
            return new OrderCommand(status, items, recipient, createdAt);
        }

        UpdateOrderCommand toUpdateOrderCommand(Long id) {
            return new UpdateOrderCommand(id, items, recipient, createdAt);
        }
    }
}
