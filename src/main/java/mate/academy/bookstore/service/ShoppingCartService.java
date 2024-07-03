package mate.academy.bookstore.service;

import mate.academy.bookstore.dto.cartitem.CreateCartItemRequestDto;
import mate.academy.bookstore.dto.cartitem.UpdateCartItemRequestDto;
import mate.academy.bookstore.dto.shoppingcart.ShoppingCartDto;
import mate.academy.bookstore.model.User;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCartForUser(Long userId);

    ShoppingCartDto addItemToCart(CreateCartItemRequestDto requestDto, Long userId);

    ShoppingCartDto updateCartItem(Long cartItemId, UpdateCartItemRequestDto requestDto,
                                   Long userId);

    void removeCartItem(Long cartItemId);

    void createShoppingCart(User user);
}
