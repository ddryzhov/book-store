package mate.academy.bookstore.repository.orderitem;

import java.util.Optional;
import java.util.Set;
import mate.academy.bookstore.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<Set<OrderItem>> findAllByOrderId(Long orderId);
}
