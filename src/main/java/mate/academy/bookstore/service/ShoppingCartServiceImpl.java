package mate.academy.bookstore.service;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.cartitem.CreateCartItemRequestDto;
import mate.academy.bookstore.dto.cartitem.UpdateCartItemRequestDto;
import mate.academy.bookstore.dto.shoppingcart.ShoppingCartDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.ShoppingCartMapper;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.CartItem;
import mate.academy.bookstore.model.ShoppingCart;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.repository.book.BookRepository;
import mate.academy.bookstore.repository.cartitem.CartItemRepository;
import mate.academy.bookstore.repository.shoppingcart.ShoppingCartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    @Transactional
    public ShoppingCartDto getShoppingCartForUser(Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found "
                        + "for user id " + userId));
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartDto addItemToCart(CreateCartItemRequestDto requestDto, Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found "
                        + "for user id " + userId));

        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found for id "
                        + requestDto.getBookId()));

        CartItem cartItem = cartItemRepository.findByShoppingCartAndBook(shoppingCart, book)
                .orElseGet(() -> {
                    CartItem newCartItem = new CartItem();
                    newCartItem.setShoppingCart(shoppingCart);
                    newCartItem.setBook(book);
                    newCartItem.setQuantity(requestDto.getQuantity());
                    shoppingCart.getCartItems().add(newCartItem);
                    return newCartItem;
                });

        if (cartItem.getId() != null) {
            cartItem.setQuantity(cartItem.getQuantity() + requestDto.getQuantity());
        }

        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(shoppingCartRepository.save(shoppingCart));
    }

    @Override
    @Transactional
    public ShoppingCartDto updateCartItem(Long cartItemId, UpdateCartItemRequestDto requestDto,
                                          Long userId) {
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found "
                        + "for user id " + userId));

        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(cartItemId, cart.getId())
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found for id "
                        + cartItemId + " and cart id " + cart.getId()));

        cartItem.setQuantity(requestDto.getQuantity());
        return shoppingCartMapper.toDto(cartItemRepository.save(cartItem).getShoppingCart());
    }

    @Override
    @Transactional
    public void removeCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found for id "
                        + cartItemId));
        cartItemRepository.delete(cartItem);
    }

    @Override
    public void createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }
}
