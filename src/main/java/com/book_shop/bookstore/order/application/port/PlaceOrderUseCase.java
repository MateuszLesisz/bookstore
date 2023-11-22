package com.book_shop.bookstore.order.application.port;

import com.book_shop.bookstore.catalog.domain.Book;
import com.book_shop.bookstore.order.domain.Order;
import com.book_shop.bookstore.order.domain.OrderItem;
import com.book_shop.bookstore.order.domain.OrderStatus;
import com.book_shop.bookstore.order.domain.Recipient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;

public interface PlaceOrderUseCase {

    Order addOrder(OrderCommand createOrderCommand);
    UpdateOrderResponse updateOrder(UpdateOrderCommand updateOrderCommand);
    void removeOrder(Long id);

    @Builder
    @Value
    class PlaceOrderCommand {
        @Singular
        List<OrderItem> orderItems;
        Recipient recipient;
    }

    @Value
    class OrderCommand {
        OrderStatus status;
        List<OrderItem> items;
        Recipient recipient;
        LocalDateTime createdAt;

        public Order toOrder() {
            return new Order(status, items, recipient, createdAt);
        }
    }

    @Value
    class OrderItemCommand {

        Book book;
        Integer quantity;
    }

    @Value
    class RecipientCommand {
        String name;
        String phone;
        String street;
        String city;
        String zipCode;
        String email;

    }

    @Value
    class PlaceOrderResponse {
        boolean success;
        Long orderId;
        List<String> errors;

    }

    @Value
    @AllArgsConstructor
    class UpdateOrderCommand {

        Long id;
        List<OrderItem> items;
        Recipient recipient;
        LocalDateTime createdAt;

        public Order updateFields(Order order) {
            if (items != null) {
                order.setItems(items);
            }
            if (recipient != null) {
                order.setRecipient(recipient);
            }
            if (createdAt != null) {
                order.setCreatedAt(LocalDateTime.now());
            }
            return order;
        }
    }

    @Value
    class UpdateOrderResponse {
        public static final UpdateOrderResponse SUCCESS = new UpdateOrderResponse(true, emptyList());

        boolean success;
        List<String> errors;
    }
}
