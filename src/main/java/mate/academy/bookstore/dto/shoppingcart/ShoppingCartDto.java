package mate.academy.bookstore.dto.shoppingcart;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import mate.academy.bookstore.dto.cartitem.CartItemDto;

@Getter
@Setter
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private Set<CartItemDto> cartItems;
}
