package com.book_shop.bookstore.order.application.port;

import com.book_shop.bookstore.catalog.domain.Book;
import com.book_shop.bookstore.order.domain.Order;
import com.book_shop.bookstore.order.domain.OrderItem;
import com.book_shop.bookstore.order.domain.OrderStatus;
import com.book_shop.bookstore.order.domain.Recipient;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

public interface PlaceOrderUseCase {

    PlaceOrderResponse placeOrder(PlaceOrderCommand placeOrderCommand);
    List<Order> findAll();
    Optional<Order> findById(Long id);

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

        Order toOrder() {
            return Order.builder()
                    .status(status)
                    .items(items)
                    .recipient(recipient)
                    .createdAt(createdAt)
                    .build();
        }
    }

    @Value
    class OrderItemCommand {
        Book book;
        Integer quantity;

        public OrderItem toOrderItem() {
            return OrderItem.builder()
                    .book(book)
                    .quantity(quantity)
                    .build();
        }
    }

    @Value
    class RecipientCommand {
        String name;
        String phone;
        String street;
        String city;
        String zipCode;
        String email;

        public Recipient toRecipient() {
            return Recipient.builder()
                    .name(name)
                    .phone(phone)
                    .street(street)
                    .city(city)
                    .zipCode(zipCode)
                    .email(email)
                    .build();
        }

    }

    @Value
    class PlaceOrderResponse {
        boolean success;
        Long orderId;
        List<String> errors;

        public static PlaceOrderResponse success(Long orderId) {
            return new PlaceOrderResponse(true, orderId, emptyList());
        }

        public static PlaceOrderResponse failure(String... errors) {
            return new PlaceOrderResponse(false, null, Arrays.asList(errors));
        }
    }
}
