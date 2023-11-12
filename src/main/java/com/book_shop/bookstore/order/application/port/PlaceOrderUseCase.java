package com.book_shop.bookstore.order.application.port;

import com.book_shop.bookstore.order.domain.OrderItem;
import com.book_shop.bookstore.order.domain.Recipient;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;

public interface PlaceOrderUseCase {

    PlaceOrderResponse placeOrder(PlaceOrderCommand placeOrderCommand);

    @Builder
    @Value
    class PlaceOrderCommand {
        @Singular
        List<OrderItem> orderItems;
        Recipient recipient;
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
