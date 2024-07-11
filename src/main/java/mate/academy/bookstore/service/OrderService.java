package mate.academy.bookstore.service;

import java.util.List;
import java.util.Set;
import mate.academy.bookstore.dto.order.CreateOrderRequestDto;
import mate.academy.bookstore.dto.order.OrderDto;
import mate.academy.bookstore.dto.orderitem.OrderItemDto;
import mate.academy.bookstore.model.Order;

public interface OrderService {
    List<OrderDto> getOrders(Long userId);

    OrderDto createOrder(Long userId, CreateOrderRequestDto createOrderRequestDto);

    OrderDto updateOrderStatus(Long orderId, Order.Status status);

    Set<OrderItemDto> getOrderItems(Long orderId);

    OrderItemDto getOrderItem(Long orderId, Long itemId);
}
