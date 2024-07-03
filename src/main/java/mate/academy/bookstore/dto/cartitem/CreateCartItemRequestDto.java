package mate.academy.bookstore.dto.cartitem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCartItemRequestDto {
    @NotNull
    @Positive
    private Long bookId;

    @Positive
    private int quantity;
}
