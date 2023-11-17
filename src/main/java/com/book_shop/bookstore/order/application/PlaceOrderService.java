package com.book_shop.bookstore.order.application;

import com.book_shop.bookstore.order.application.port.PlaceOrderUseCase;
import com.book_shop.bookstore.order.domain.Order;
import com.book_shop.bookstore.order.domain.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PlaceOrderService implements PlaceOrderUseCase {

    private final OrderRepository orderRepository;

    @Override
    public PlaceOrderResponse placeOrder(PlaceOrderCommand placeOrderCommand) {
        Order order = Order.builder()
                .recipient(placeOrderCommand.getRecipient())
                .items(placeOrderCommand.getOrderItems())
                .build();
        Order save = orderRepository.save(order);
        return PlaceOrderResponse.success(save.getId());
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }
}
