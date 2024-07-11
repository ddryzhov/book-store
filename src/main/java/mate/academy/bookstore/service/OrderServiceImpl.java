package mate.academy.bookstore.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.order.CreateOrderRequestDto;
import mate.academy.bookstore.dto.order.OrderDto;
import mate.academy.bookstore.dto.orderitem.OrderItemDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.OrderItemMapper;
import mate.academy.bookstore.mapper.OrderMapper;
import mate.academy.bookstore.model.Order;
import mate.academy.bookstore.model.OrderItem;
import mate.academy.bookstore.model.ShoppingCart;
import mate.academy.bookstore.repository.order.OrderRepository;
import mate.academy.bookstore.repository.orderitem.OrderItemRepository;
import mate.academy.bookstore.repository.shoppingcart.ShoppingCartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    @Transactional
    public List<OrderDto> getOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("No orders found for user with id "
                        + userId));

        return orderMapper.map(orders);
    }

    @Override
    @Transactional
    public OrderDto createOrder(Long userId, CreateOrderRequestDto createOrderRequestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found "
                        + "for user id " + userId));

        BigDecimal total = calculateTotal(shoppingCart);

        Order order = createNewOrder(shoppingCart, total,
                createOrderRequestDto.getShippingAddress());

        createAndSaveOrderItems(shoppingCart, order);

        clearShoppingCart(shoppingCart);

        OrderDto orderDto = orderMapper.toDto(order);
        Set<OrderItemDto> orderItemDtos = getOrderItems(order.getId());
        orderDto.setOrderItems(orderItemDtos);
        return orderDto;
    }

    private Order createNewOrder(ShoppingCart shoppingCart, BigDecimal total,
                                 String shippingAddress) {
        Order order = new Order();
        order.setUser(shoppingCart.getUser());
        order.setStatus(Order.Status.PENDING);
        order.setTotal(total);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(shippingAddress);
        return orderRepository.save(order);
    }

    private void createAndSaveOrderItems(ShoppingCart shoppingCart, Order order) {
        shoppingCart.getCartItems().forEach(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getBook().getPrice());
            orderItemRepository.save(orderItem);
        });
    }

    private void clearShoppingCart(ShoppingCart shoppingCart) {
        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    @Transactional
    public OrderDto updateOrderStatus(Long orderId, Order.Status status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        order.setStatus(status);
        orderRepository.save(order);

        return orderMapper.toDto(order);
    }

    @Override
    @Transactional
    public Set<OrderItemDto> getOrderItems(Long orderId) {
        Set<OrderItem> orderItems = orderItemRepository.findAllByOrderId(orderId)
                .orElseThrow(() -> new EntityNotFoundException("No items found for order with id "
                        + orderId));

        return orderItemMapper.map(orderItems);
    }

    @Override
    @Transactional
    public OrderItemDto getOrderItem(Long orderId, Long itemId) {
        OrderItem orderItem = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("OrderItem not found"));

        if (!orderItem.getOrder().getId().equals(orderId)) {
            throw new IllegalArgumentException("OrderItem does not belong to the specified Order");
        }

        return orderItemMapper.toDto(orderItem);
    }

    private BigDecimal calculateTotal(ShoppingCart shoppingCart) {
        return shoppingCart.getCartItems().stream()
                .map(cartItem -> cartItem.getBook().getPrice()
                        .multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
