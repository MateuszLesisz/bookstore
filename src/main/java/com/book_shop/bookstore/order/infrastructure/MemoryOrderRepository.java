package com.book_shop.bookstore.order.infrastructure;

import com.book_shop.bookstore.order.domain.Order;
import com.book_shop.bookstore.order.domain.OrderRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MemoryOrderRepository implements OrderRepository {
    private final Map<Long, Order> storage = new ConcurrentHashMap<>();
    private final AtomicLong NEXT_ID = new AtomicLong(0L);


    @Override
    public Order save(Order order) {
        if (order.getId() != null) {
            storage.put(order.getId(), order);
        } else {
            long nextId = nextId();
            order.setId(nextId);
            order.setCreatedAt(LocalDateTime.now());
        }
        return order;
    }

    private long nextId() {
        return NEXT_ID.incrementAndGet();
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(storage.values());
    }
}
