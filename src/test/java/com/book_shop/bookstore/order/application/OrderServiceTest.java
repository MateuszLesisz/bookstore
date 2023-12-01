package com.book_shop.bookstore.order.application;

import com.book_shop.bookstore.catalog.application.port.CatalogUseCase;
import com.book_shop.bookstore.catalog.db.BookJpaRepository;
import com.book_shop.bookstore.catalog.domain.Book;
import com.book_shop.bookstore.order.application.port.ManipulateOrderUseCase.OrderItemCommand;
import com.book_shop.bookstore.order.application.port.ManipulateOrderUseCase.PlaceOrderCommand;
import com.book_shop.bookstore.order.application.port.ManipulateOrderUseCase.PlaceOrderResponse;
import com.book_shop.bookstore.order.application.port.QueryOrderUseCase;
import com.book_shop.bookstore.order.domain.OrderStatus;
import com.book_shop.bookstore.order.domain.Recipient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import java.math.BigDecimal;

import static com.book_shop.bookstore.order.application.port.ManipulateOrderUseCase.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class OrderServiceTest {

    @Autowired
    BookJpaRepository bookRepository;
    @Autowired
    ManipulateOrderService orderService;
    @Autowired
    CatalogUseCase catalogUseCase;
    @Autowired
    QueryOrderUseCase queryOrderUseCase;

    @Test
    public void userCanPlaceOrder() {
        //given
        Book effectiveJava = givenEffectiveJava(50L);
        Book jcip = givenJavaConcurrency(50L);
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient())
                .orderItem(new OrderItemCommand(effectiveJava.getId(), 10))
                .orderItem(new OrderItemCommand(jcip.getId(), 10))
                .build();

        //when
        PlaceOrderResponse response = orderService.placeOrder(command);

        //then
        assertTrue(response.isSuccess());
        assertEquals(40L, availableCopiesOf(effectiveJava));
        assertEquals(40L, availableCopiesOf(jcip));
    }

    @Test
    public void userCantOrderMoreBooksThanAvailable() {
        //given
        Book effectiveJava = givenEffectiveJava(5L);
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient())
                .orderItem(new OrderItemCommand(effectiveJava.getId(), 10))
                .build();

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.placeOrder(command);
        });

        //then
        assertTrue(exception.getMessage().contains("Too many copies of book " + effectiveJava.getId() + " requested"));
    }

    @Test
    public void userCanRevokeOrder() {
        //given
        Book effectiveJava = givenEffectiveJava(50L);
        String recipient = "marek@example.org";
        Long orderId = placedOrder(effectiveJava.getId(), 15, recipient);
        assertEquals(35L, availableCopiesOf(effectiveJava));

        //when
        UpdateStatusCommand command = new UpdateStatusCommand(orderId, OrderStatus.CANCELED, recipient);
        orderService.updateOrderStatus(command);

        //then
        assertEquals(50L, availableCopiesOf(effectiveJava));
        assertEquals(OrderStatus.CANCELED, queryOrderUseCase.findById(orderId).get().getStatus());
    }

    @Test
    public void userCannotRevokePaidOrder() {
        //given
        Book effectiveJava = givenEffectiveJava(50L);
        Long orderId = placeOrder(effectiveJava.getId(), 15);
        assertEquals(35L, availableCopiesOf(effectiveJava));

        // TODO: fix on security module
        UpdateStatusCommand command = new UpdateStatusCommand(orderId, OrderStatus.PAID, "john@example.org");
        orderService.updateOrderStatus(command);

        //when
        UpdateStatusCommand command2 = new UpdateStatusCommand(orderId, OrderStatus.CANCELED, "john@example.org");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.updateOrderStatus(command2);
        });

        //then
        assertEquals(exception.getMessage(), "Unable to mark " + OrderStatus.PAID + " order as " + OrderStatus.CANCELED);

    }

    @Test
    public void userCannotRevokeShippedOrder() {
        //given
        Book effectiveJava = givenEffectiveJava(50L);
        Long orderId = placeOrder(effectiveJava.getId(), 15);
        assertEquals(35L, availableCopiesOf(effectiveJava));
        UpdateStatusCommand command = new UpdateStatusCommand(orderId, OrderStatus.PAID, "john@example.org");
        UpdateStatusCommand command2 = new UpdateStatusCommand(orderId, OrderStatus.SHIPPED, "john@example.org");
        UpdateStatusCommand command3 = new UpdateStatusCommand(orderId, OrderStatus.CANCELED, "john@example.org");
        orderService.updateOrderStatus(command);
        orderService.updateOrderStatus(command2);


        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.updateOrderStatus(command3);
        });

        //then
        assertEquals(exception.getMessage(), "Unable to mark " + OrderStatus.SHIPPED + " order as " + OrderStatus.CANCELED);

    }

    @Test
    public void userCannotOrderNoExistingBook() {
        //given
        Book effectiveJava = givenEffectiveJava(5L);
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient())
                .orderItem(new OrderItemCommand(effectiveJava.getId(), 10))
                .build();

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.placeOrder(command);
        });

        //then
        assertEquals(exception.getMessage(), "Too many copies of book 1 requested 10 of 5 available.");
    }

    @Test
    public void userCannotOrderNegativeNumberOfBooks() {
        //given
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient())
                .orderItem(new OrderItemCommand(20L, 10))
                .build();

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.placeOrder(command);
        });

        //then
        assertEquals(exception.getMessage(), "Book does not exist.");
    }

    @Test
    public void userCannotRevokeOtherUserOrder() {
        //given
        Book effectiveJava = givenEffectiveJava(50L);
        String recipient = "adam@example.org";
        Long orderId = placedOrder(effectiveJava.getId(), 15, recipient);
        assertEquals(35L, availableCopiesOf(effectiveJava));

        //when
        UpdateStatusCommand command = new UpdateStatusCommand(orderId, OrderStatus.CANCELED, "marek@example.org");
        orderService.updateOrderStatus(command);

        //then
        assertEquals(35, availableCopiesOf(effectiveJava));
        assertEquals(OrderStatus.NEW, queryOrderUseCase.findById(orderId).get().getStatus());

    }

    public Long placedOrder(Long bookId, int copies, String recipient) {
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient(recipient))
                .orderItem(new OrderItemCommand(bookId, copies))
                .build();
        return orderService.placeOrder(command).getRight();
    }

    public Long placedOrder(Long bookId, int copies) {
        return placedOrder(bookId, copies, "john@example.org");
    }

    private Book givenJavaConcurrency(long available) {
        return bookRepository.save(new Book("Java concurrency in Practice", 2006, new BigDecimal("99.90"), available));
    }

    private Book givenEffectiveJava(long available) {
        return bookRepository.save(new Book("Effective Java", 2005, new BigDecimal("199.90"), available));
    }

    private Recipient recipient() {
        return Recipient.builder().email("john@example.org").build();
    }

    private Recipient recipient(String email) {
        return Recipient.builder().email(email).build();
    }

    public Long placeOrder(Long bookId, int copies) {
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient())
                .orderItem(new OrderItemCommand(bookId, copies))
                .build();
        return orderService.placeOrder(command).getRight();
    }

    private Long availableCopiesOf(Book book) {
        return catalogUseCase.findById(book.getId()).get().getAvailable();
    }
}