package mate.academy.bookstore.repository.cartitem;

import java.util.Optional;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.CartItem;
import mate.academy.bookstore.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByIdAndShoppingCartId(Long id, Long shoppingCartId);

    Optional<CartItem> findByShoppingCartAndBook(ShoppingCart shoppingCart, Book book);
}
