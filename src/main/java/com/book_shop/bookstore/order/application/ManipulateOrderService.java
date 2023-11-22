package com.book_shop.bookstore.order.application;

import com.book_shop.bookstore.order.application.port.PlaceOrderUseCase;
import com.book_shop.bookstore.order.db.OrderJpaRepository;
import com.book_shop.bookstore.order.domain.Order;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
@AllArgsConstructor
public class ManipulateOrderService implements PlaceOrderUseCase {

    private final OrderJpaRepository orderRepository;

    @Override
    public Order addOrder(OrderCommand orderCommand) {
        return orderRepository.save(orderCommand.toOrder());
    }

    @Override
    public UpdateOrderResponse updateOrder(UpdateOrderCommand updateOrder) {
        return orderRepository.findById(updateOrder.getId())
                .map(order -> {
                    Order updatedOrder = updateOrder.updateFields(order);
                    orderRepository.save(updatedOrder);
                    return UpdateOrderResponse.SUCCESS;
                })
                .orElseGet(() -> new UpdateOrderResponse(false, Collections.singletonList("Order not found with id: " + updateOrder.getId())));
    }

    @Override
    public void removeOrder(Long id) {
        orderRepository.deleteById(id);
    }
}