package mate.academy.bookstore.service;

import mate.academy.bookstore.dto.cartitem.CreateCartItemRequestDto;
import mate.academy.bookstore.dto.cartitem.UpdateCartItemRequestDto;
import mate.academy.bookstore.dto.shoppingcart.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCartForUser();

    ShoppingCartDto addItemToCart(CreateCartItemRequestDto requestDto);

    ShoppingCartDto updateCartItem(Long cartItemId, UpdateCartItemRequestDto requestDto);

    void removeCartItem(Long cartItemId);
}
