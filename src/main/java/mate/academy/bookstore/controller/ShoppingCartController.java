package mate.academy.bookstore.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.cartitem.CreateCartItemRequestDto;
import mate.academy.bookstore.dto.cartitem.UpdateCartItemRequestDto;
import mate.academy.bookstore.dto.shoppingcart.ShoppingCartDto;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.service.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartDto getShoppingCart() {
        Long userId = getCurrentUserId();
        return shoppingCartService.getShoppingCartForUser(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartDto addItemToCart(@Valid @RequestBody CreateCartItemRequestDto requestDto) {
        Long userId = getCurrentUserId();
        return shoppingCartService.addItemToCart(requestDto, userId);
    }

    @PutMapping("/items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartDto updateCartItem(@PathVariable Long cartItemId,
                                          @Valid @RequestBody UpdateCartItemRequestDto requestDto) {
        Long userId = getCurrentUserId();
        return shoppingCartService.updateCartItem(cartItemId, requestDto, userId);
    }

    @DeleteMapping("/items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('USER')")
    public void removeCartItem(@PathVariable Long cartItemId) {
        shoppingCartService.removeCartItem(cartItemId);
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }
}
